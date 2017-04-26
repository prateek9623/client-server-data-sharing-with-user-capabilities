/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Prateek
 */
public class getfilelist extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionid = session.getId();
            dbconnect connect = dbconnect.dbconnectref();
            if (connect.update_session(sessionid)) {
                List<file> filelist = new ArrayList<>();
                JSONArray filelistArray = new JSONArray();
                if (connect.getfilelist(sessionid, filelist)) {
                    for (int i = 0; i < filelist.size(); i++) {
                        JSONObject obj = new JSONObject();
                        obj.put("file_id", filelist.get(i).getFile_id());
                        obj.put("file_name", filelist.get(i).getFile_name());
                        obj.put("file_size", filelist.get(i).getFile_size());
                        obj.put("stored_on", filelist.get(i).getStored_on());
                        obj.put("ownerusername", filelist.get(i).getOwnerusername());
                        obj.put("sharedto", filelist.get(i).getSharedto());
                        obj.put("shared", filelist.get(i).getShared());
                        obj.put("predelete", filelist.get(i).getPredelete());
                        obj.put("directory", filelist.get(i).getDirectory());
                        filelistArray.add(obj);
                    }
                }
                response.getWriter().write(filelistArray.toString());
                response.setStatus(response.SC_ACCEPTED);
            } else {
                response.setStatus(response.SC_GATEWAY_TIMEOUT);
            }
        }else{
            response.setStatus(response.SC_FORBIDDEN);
        }

    }

}
