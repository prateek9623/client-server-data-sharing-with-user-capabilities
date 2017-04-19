/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import java.io.File;
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
public class rename extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionid = (String) request.getParameter("sessionid");
        String fileid = (String) request.getParameter("fileid");
        String newname = (String) request.getParameter("newname");
        dbconnect db = dbconnect.dbconnectref();
        if(db.update_session(sessionid)){
            String filepath = db.getFilePath(sessionid, fileid);
            File file = new File(filepath);
            File newfile = new File(filepath.substring(0,filepath.lastIndexOf("\\")+1)+newname+filepath.substring(filepath.lastIndexOf(".")));
            if(!newfile.exists()){
                file.renameTo(newfile);
                if(db.updateFileDetails(sessionid,newfile.getPath().replace("\\", "\\\\"),newfile.getName(),fileid,newfile.length()+"")){
                    response.setStatus(response.SC_ACCEPTED);
                }else{
                    newfile.renameTo(file);
                    response.setStatus(response.SC_CONFLICT);
                }
            }else{
                response.setStatus(response.SC_CONFLICT);
            }
        }else{
            response.setStatus(response.SC_FORBIDDEN);
        }
    }
}
