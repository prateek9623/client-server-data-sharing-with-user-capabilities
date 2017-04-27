/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Prateek
 */
public class encrypt extends HttpServlet {

    protected String temploc = null;
    protected String uploadloc = null;

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
            String fileid = (String) request.getParameter("fileid");
            String userpass = (String) request.getParameter("userpass");
            String encryptpass = (String) request.getParameter("newencryptpass");
            dbconnect db = dbconnect.dbconnectref();
            if (db.update_session(sessionid)) {
                String filepath = db.getFilePath(sessionid, fileid);
                if (!filepath.endsWith(".aes")) {
                    File file = new File(filepath);
                    File newfle = new File(filepath + ".aes");
                    if (!newfle.exists()) {
                        String filename = db.check_password(sessionid, userpass);
                        File tempfile = new File(temploc + filename);
                        FileInputStream fileInputStream = new FileInputStream(file);
                        FileOutputStream tempfileOutputStream = new FileOutputStream(tempfile);
                            try {
                            AES_1.encrypt(128,encryptpass.toCharArray(), fileInputStream, tempfileOutputStream);
                            Files.copy(tempfile.toPath(), newfle.toPath());
                            db.updateFileDetails(sessionid, newfle.getPath().replace("\\", "\\\\"), newfle.getName(), fileid, newfle.length() + "");
                            response.setStatus(response.SC_ACCEPTED);
                        } catch (AES_1.InvalidKeyLengthException ex) {
                            response.setStatus(response.SC_PARTIAL_CONTENT);
                            newfle.delete();
                            return;
                        } catch (AES_1.StrongEncryptionNotAvailableException ex) {
                            response.setStatus(response.SC_EXPECTATION_FAILED);
                            newfle.delete();
                            return;
                        }finally{
                            fileInputStream.close();
                            tempfileOutputStream.close();
                            tempfile.delete();
                        }
                        file.delete();
                    } else {
                        response.setStatus(response.SC_CONFLICT);
                    }
                } else {
                    response.setStatus(response.SC_CONFLICT);
                }
            } else {
                response.setStatus(response.SC_FORBIDDEN);
            }
        } else {
            response.setStatus(response.SC_FORBIDDEN);
        }
    }
}
