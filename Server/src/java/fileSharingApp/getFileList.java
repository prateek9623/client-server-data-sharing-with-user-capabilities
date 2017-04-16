package fileSharingApp;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

@WebServlet(name = "getFileList", urlPatterns = {"/getFileList"})
public class getFileList extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionid = request.getParameter("sessionid");
        dbconnect connect = dbconnect.dbconnectref();
        if (connect.update_session(sessionid)) {
            response.setStatus(response.SC_ACCEPTED);
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
                    obj.put("shared", filelist.get(i).getShared());
                    filelistArray.add(obj);
                }
            }
            response.getWriter().write(filelistArray.toString());

        } else {
            response.setStatus(response.SC_FORBIDDEN);
        }
    }

}
