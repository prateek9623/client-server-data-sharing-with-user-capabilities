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
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

/**
 *
 * @author Prateek
 */
@MultipartConfig
public class upload extends HttpServlet {

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
        dbconnect db = dbconnect.dbconnectref();
        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionid = session.getId();
            if (db.update_session(sessionid)) {
                String username = db.check_session(sessionid);
                Part filePart = request.getPart("file");
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                try (InputStream fileContent = filePart.getInputStream()) {
                    boolean checkEncrypt = ((String) request.getParameter("checkEncrypt")).trim().equals("true");
                    if (checkEncrypt) {
                        fileName += ".aes";
                    }
                    String filepath = uploadloc + username + "\\\\" + fileName;
                    boolean exists = Files.exists(Paths.get(filepath));
                    File newfile = new File(filepath);
                    if (exists) {
                        response.setStatus(response.SC_CONFLICT);
                        return;
                    }
                    newfile.getParentFile().mkdirs();
                    if (checkEncrypt) {
                        String encryptPass = (String) request.getParameter("pass");
                        File tempfile = new File(temploc + fileName);
                        tempfile.createNewFile();
                        Files.copy(fileContent, tempfile.toPath());
                        FileInputStream tempFileInputStream = new FileInputStream(tempfile);
                        FileOutputStream fileOutputStream = new FileOutputStream(newfile);
                        try {
                            AES_1.encrypt(128, encryptPass.toCharArray(), tempFileInputStream, fileOutputStream);
                        } catch (AES_1.InvalidKeyLengthException ex) {
                            response.setStatus(response.SC_PARTIAL_CONTENT);
                            fileOutputStream.close();
                            newfile.delete();
                            return;
                        } catch (AES_1.StrongEncryptionNotAvailableException ex) {
                            response.setStatus(response.SC_EXPECTATION_FAILED);
                            fileOutputStream.close();
                            newfile.delete();
                            return;
                        }finally{
                            tempFileInputStream.close();
                            fileOutputStream.close();
                            tempfile.delete();
                        }
                    } else {
                        Files.copy(fileContent, newfile.toPath());
                    }
                    if (db.uploadfileentry(newfile.getName(), sessionid, newfile.length() + "", filepath)) {
                        response.setStatus(response.SC_ACCEPTED);
                    } else {
                        newfile.delete();
                        response.setStatus(response.SC_NO_CONTENT);
                    }
                }
            } else {
                response.setStatus(response.SC_UNAUTHORIZED);
            }
        } else {
            response.setStatus(response.SC_UNAUTHORIZED);
        }
    }

    private String randGenerator() {
        SecureRandom random = new SecureRandom();
        String random_string = new BigInteger(32, random).toString(32);
        return random_string;
    }
}
