/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Prateek
 */
public class remove extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionid = session.getId();
            String shareid = (String) request.getParameter("fileid");
            String pass = (String) request.getParameter("password");
            dbconnect db = dbconnect.dbconnectref();
            if (db.update_session(sessionid)) {
                String userid = db.check_password(sessionid, pass);
                if (userid.equals("")) {
                    response.setStatus(response.SC_FORBIDDEN);
                } else {
                    if (db.unShare(userid, shareid)) {
                        response.setStatus(response.SC_ACCEPTED);
                    } else {
                        response.setStatus(response.SC_BAD_REQUEST);
                    }
                }
            } else {
                response.setStatus(response.SC_FORBIDDEN);
            }
        } else {
            response.setStatus(response.SC_FORBIDDEN);
        }
    }

}
