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

/**
 *
 * @author Prateek
 */
public class predelete extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionid = (String) request.getParameter("sessionid");
        String fileid = (String) request.getParameter("fileid");
        dbconnect db = dbconnect.dbconnectref();
        if(db.update_session(sessionid)&&db.predel(sessionid,fileid)){
            response.setStatus(response.SC_ACCEPTED);
        }else{
            response.setStatus(response.SC_FORBIDDEN);
        }
    }
}
