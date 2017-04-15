package fileSharingApp;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        if(connect.update_session(sessionid)){
            ResultSet rs= connect.getfilelist(sessionid);
            response.setStatus(response.SC_ACCEPTED);
            try {
                rs.first();
                JSONArray filelistArray = new JSONArray();
                while(!rs.isAfterLast()){
                    JSONObject obj = new JSONObject();
                    obj.put("file_id", rs.getString(1));
                    obj.put("file_name", rs.getString(2));
                    obj.put("file_size", rs.getString(4));
                    obj.put("stored_on", rs.getString(5));
                    obj.put("path", rs.getString(6).substring(rs.getString(6).indexOf("12345")+5));
                    filelistArray.add(obj);
                    rs.next();
//                    response.setContentType("application/json");
//                    response.getOutputStream().print(obj.toJSONString());
                }
//                response.setContentType("application/json");
                response.getWriter().write(filelistArray.toString());
            } catch (SQLException ex) {
                
            }
            
        }else{
            response.setStatus(response.SC_FORBIDDEN);
        }
   }

}
