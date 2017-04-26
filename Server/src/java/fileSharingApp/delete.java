/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import java.io.File;
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
public class delete extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionid = session.getId();
            dbconnect db = dbconnect.dbconnectref();
            if (db.update_session(sessionid)) {
                String deletefile = (String) request.getParameter("deletefile");
                String password = (String) request.getParameter("password");
                String filepath = db.delete(sessionid, deletefile, password);
                if (!filepath.equals("") || !filepath.equals("null")) {
                    File file = new File(filepath);
                    file.delete();
                    response.setStatus(response.SC_ACCEPTED);
                } else {
                    response.setStatus(response.SC_CONFLICT);
                }
            } else {
                response.setStatus(response.SC_CONFLICT);
            }
        }
    }
}
