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

/**
 *
 * @author Prateek
 */
public class delete extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionid = (String) request.getParameter("sessionid");
        String deletefile = (String) request.getParameter("deletefile");
        dbconnect db = dbconnect.dbconnectref();
        if (db.update_session(sessionid)) {
            System.out.println(sessionid+deletefile);
            String filepath;
            if(!(filepath = db.delete(sessionid, deletefile)).equals("")){
                File file = new File(filepath);
                file.delete();
                System.out.println(sessionid+deletefile);
                response.setStatus(response.SC_ACCEPTED);
            }
            else{
                response.setStatus(response.SC_CONFLICT);
            }
        }else{
            response.setStatus(response.SC_CONFLICT);
        }
    }

}
