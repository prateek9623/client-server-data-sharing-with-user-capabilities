/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
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
        System.out.println("user:"+user+"pass:"+pass);
        HttpSession session = request.getSession();
        if(db.authenticate(user, pass)){
            sessionid = session.getId();
            db.create_session(user, sessionid);
            session.setAttribute("uname", user);
        }else{
            session.invalidate();
            sessionid = "WRONGPASS";
        }
        System.out.println(sessionid);
        response.getOutputStream().println(sessionid);    
    }
}
