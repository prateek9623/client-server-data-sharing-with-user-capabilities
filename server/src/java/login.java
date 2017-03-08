/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Prateek
 */
public class login extends HttpServlet {
        @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        dbconnect db = new dbconnect();
        String sessionid;
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        System.out.println("user:"+user+"pass:"+pass);
        if(db.authenticate(user, pass)){
            sessionid = sidGenerator();
            db.create_session(user, sessionid);
        }else{
            sessionid = null;
        }
        response.getOutputStream().println(sessionid);
        while(db.check_session(sessionid)){
            try {
                Thread.sleep(30*60*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(login.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        db.delete_session(sessionid);
    }
    private String sidGenerator(){
        SecureRandom random = new SecureRandom();
        String random_string = new BigInteger(130,random).toString(32);
        return random_string;
    }
}
