/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Prateek
 */
public class register extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("username");
        String pass = request.getParameter("password");
        String fname = request.getParameter("fname");
        String lname = request.getParameter("lname");
        String phone = request.getParameter("phone");
        String email = request.getParameter("email");
        String gender = request.getParameter("gender");
        String dob = request.getParameter("dob");
        System.out.println("user:"+user+
                "\npass:"+pass+
                "\nfname:"+fname+
                "\nlname:"+lname+
                "\nphone:"+phone+
                "\nemail"+email+
                "gender:"+gender+
                "dob"+dob);
        dbconnect db = dbconnect.dbconnectref();
        if(db.register(user, pass, fname, lname, email, phone, dob, gender)){
            response.getOutputStream().println("SUCCESS");
        }else{
            response.getOutputStream().println("FAILED");
        }
    }
}
