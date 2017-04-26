/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Prateek
 */
public class decrypt extends HttpServlet {

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
            String encryptpass = (String) request.getParameter("oldpass");
            dbconnect db = dbconnect.dbconnectref();
            if (db.update_session(sessionid)) {
                String filepath = db.getFilePath(sessionid, fileid);
                if (filepath.endsWith(".aes")) {
                    File file = new File(filepath);
                    File newfle = new File(filepath.substring(0, filepath.indexOf(".aes")));
                    if (!newfle.exists()) {
                        String filename = db.check_password(sessionid, userpass);
                        File tempfile = new File(temploc + filename);
                        try {
                            aes.decrypt(encryptpass, file, tempfile);
                            Files.copy(tempfile.toPath(), newfle.toPath());
                            db.updateFileDetails(sessionid, newfle.getPath().replace("\\", "\\\\"), newfle.getName(), fileid, newfle.length() + "");
                            response.setStatus(response.SC_ACCEPTED);
                        } catch (CryptoException ex) {
                            tempfile.delete();
                            newfle.delete();
                            response.setStatus(response.SC_NO_CONTENT);
                            System.out.println(ex.getMessage());
                            ex.printStackTrace();
                            return;
                        }
                        file.delete();
                        tempfile.delete();
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
