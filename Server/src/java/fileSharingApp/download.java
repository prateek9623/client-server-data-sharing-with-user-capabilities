package fileSharingApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class download extends HttpServlet {

    private String temploc;
    private String uploadloc;

    @Override
    public void init() {
        temploc = getServletContext().getInitParameter("temp-file-upload");
        uploadloc = getServletContext().getInitParameter("file-upload");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionid = session.getId();
            String fileid = request.getParameter("fileid");
            dbconnect connect = dbconnect.dbconnectref();
            if (connect.update_session(sessionid)) {
                String filepath;
                filepath = connect.getFilePath(sessionid, fileid);
                if (filepath != null || filepath.equals("")) {
                    File file = new File(filepath);
                    System.out.println(file.getPath());
                    ServletOutputStream outputStream = response.getOutputStream();
                    ServletContext context = getServletConfig().getServletContext();
                    FileInputStream fis;
                    File tempfile;
                    if (!file.getName().endsWith(".aes")) {
                        fis = new FileInputStream(file);
                        String mimetype = context.getMimeType(file.getPath());
                        response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
                        response.setContentLength((int) file.length());
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
                    } else {
                        tempfile = new File(temploc + file.getName().replace(".aes", ""));
                        String pass = request.getParameter("pass");
                        try {
                            aes.decrypt(pass, file, tempfile);
                            fis = new FileInputStream(tempfile);
                            String mimetype = context.getMimeType(tempfile.getPath());
                            response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
                            response.setContentLength((int) tempfile.length());
                            response.setHeader("Content-Disposition", "attachment; filename=\"" + tempfile.getName() + "\"");
                        } catch (CryptoException ex) {
                            Logger.getLogger(download.class.getName()).log(Level.SEVERE, null, ex);
                            response.setStatus(response.SC_CONFLICT);
                            return;
                        }
                    }
                    response.setStatus(response.SC_ACCEPTED);
                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    fis.close();
                    outputStream.close();

                } else {
                    response.setStatus(response.SC_BAD_REQUEST);
                }
            } else {
                response.setStatus(response.SC_FORBIDDEN);
            }
        } else {
            response.setStatus(response.SC_FORBIDDEN);
        }

    }

}
