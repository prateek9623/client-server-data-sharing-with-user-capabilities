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
            File file = new File("D:\\Uploadfiles\\"+randGenerator()+"12345"+fileName);
            Files.copy(fileContent, file.toPath());
            dbconnect db = dbconnect.dbconnectref();
            System.out.println(sessionid+"12313");
            if(db.check_session(sessionid)){
            if(db.uploadfileentry(fileName, sessionid,file.length()+"" , file.getPath())){
                response.getOutputStream().print("SUCCESS");
                db.update_session(sessionid);
            }
            else{
                response.getOutputStream().print("FAILED");
            }
        }else{
            response.getOutputStream().print("INVALID");
            System.out.println("Sessionnotcreated");
        }
    }
    private String randGenerator(){
        SecureRandom random = new SecureRandom();
        String random_string = new BigInteger(10,random).toString(32);
        return random_string;
    }
}
