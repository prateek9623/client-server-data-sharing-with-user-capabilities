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
public class updatesession extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        dbconnect db = dbconnect.dbconnectref();
        HttpSession session = request.getSession(false);
        if(session!=null){
        String sessionid = session.getId();
            if(db.update_session(sessionid)){
                response.getOutputStream().println((String) session.getAttribute("uname"));
                response.setStatus(response.SC_ACCEPTED);
            }else{
                response.setStatus(response.SC_GATEWAY_TIMEOUT);
            }
        }else{
            response.setStatus(response.SC_UNAUTHORIZED);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}