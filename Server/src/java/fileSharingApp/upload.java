/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import java.io.File;
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Part sessionidpart = request.getPart("sessionid");
        String sessionid = (String) request.getParameter("sessionid");
        Part filePart = request.getPart("file");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        InputStream fileContent = filePart.getInputStream();
        String filepath = "D:\\\\Uploadfiles\\\\" + randGenerator() + fileName;
        File file = new File(filepath);
        Files.copy(fileContent, file.toPath());
        dbconnect db = dbconnect.dbconnectref();
        System.out.println(sessionid + "12313");
        if (db.update_session(sessionid)&&db.uploadfileentry(fileName, sessionid, file.length()+"", filepath)) {
            response.setStatus(response.SC_ACCEPTED);
        }else{
            file.delete();
            response.setStatus(response.SC_CONFLICT);
        }  
    }

    private String randGenerator() {
        SecureRandom random = new SecureRandom();
        String random_string = new BigInteger(32, random).toString(32);
        return random_string;
    }
}
