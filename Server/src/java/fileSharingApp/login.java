/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Prateek
 */
public class login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        dbconnect db = dbconnect.dbconnectref();
        String sessionid;
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        HttpSession session = request.getSession();
        if(!db.check_userexist(user).equals("")){
            if(db.authenticate(user, pass)){
                sessionid = session.getId();
                db.create_session(user, sessionid);
                session.setAttribute("uname", user);
                response.getOutputStream().println(sessionid); 
                response.setStatus(response.SC_ACCEPTED);
            }else{
                session.invalidate();
                response.setStatus(response.SC_UNAUTHORIZED);
            }
        }else{
            response.setStatus(response.SC_NON_AUTHORITATIVE_INFORMATION);
        }   
    }
}
