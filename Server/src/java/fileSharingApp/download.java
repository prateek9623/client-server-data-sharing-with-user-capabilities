package fileSharingApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
                    ServletOutputStream outputStream = response.getOutputStream();
                    ServletContext context = getServletConfig().getServletContext();
                    FileInputStream fis = new FileInputStream(file);
                    FileInputStream newFileInputStream;
                    File tempfile;
                    if (!file.getName().endsWith(".aes")) {
                        newFileInputStream = new FileInputStream(file);
                        String mimetype = context.getMimeType(file.getPath());
                        response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
                        response.setContentLength((int) file.length());
                        response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
                    } else {
                        tempfile = new File(temploc + file.getName().replace(".aes", ""));
                        FileOutputStream tempFileOutputStream = new FileOutputStream(tempfile);
                        String pass = request.getParameter("pass");
                        try {
                            AES_1.decrypt(pass.toCharArray(), fis, tempFileOutputStream);
                            newFileInputStream = new FileInputStream(tempfile);
                            String mimetype = context.getMimeType(tempfile.getPath());
                            response.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
                            response.setContentLength((int) tempfile.length());
                            response.setHeader("Content-Disposition", "attachment; filename=\"" + tempfile.getName() + "\"");
                        } catch (AES_1.InvalidPasswordException ex) {
                            response.setStatus(response.SC_UNAUTHORIZED);
                            outputStream.close();
                            return;
                        } catch (AES_1.InvalidAESStreamException ex) {
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            outputStream.close();
                            return;
                        } catch (AES_1.StrongEncryptionNotAvailableException ex) {
                            response.setStatus(HttpServletResponse.SC_CONFLICT);
                            outputStream.close();
                            return;
                        } finally{
                            tempFileOutputStream.close();
                            fis.close();
                        }
                    }
                    response.setStatus(response.SC_ACCEPTED);
                    byte[] buffer = new byte[4096];
                    int bytesRead = -1;
                    while ((bytesRead = newFileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    newFileInputStream.close();
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
