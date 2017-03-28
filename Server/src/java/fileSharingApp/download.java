package fileSharingApp;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
public class download extends HttpServlet {

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String sessionid = request.getParameter("sessionid");
        String fileid = request.getParameter("fileid");
        dbconnect connect = dbconnect.dbconnectref();
        String filepath;
        filepath =connect.getFilePath(sessionid,fileid);
        if(filepath!=null||filepath.equals("")){
            connect.update_session(sessionid);
            File file = new File(filepath);
            System.out.println(file.getPath());
            FileInputStream fis = new FileInputStream(file);
            ServletOutputStream outputStream = response.getOutputStream();
            ServletContext context = getServletConfig().getServletContext();
            String mimetype = context.getMimeType(filepath);
            response.setStatus(response.SC_ACCEPTED);
            response.setContentType((mimetype!=null)?mimetype:"application/octet-stream");
            response.setContentLength((int)file.length());
            response.setHeader("Content-Disposition","attachment; filename=\""+file.getName().substring(7)+"\"");            
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while((bytesRead = fis.read(buffer))!=-1){
                outputStream.write(buffer,0,bytesRead);
            }
            fis.close();
            outputStream.close();
            
        }else{
            response.setStatus(response.SC_BAD_REQUEST);
        }
        
    }

}
