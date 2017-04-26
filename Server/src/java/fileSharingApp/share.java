package fileSharingApp;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class share extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String sessionid = session.getId();
            String fileid = (String) request.getParameter("fileid");
            String sharetousername = (String) request.getParameter("sharedtoid");
            String sharerpassword = (String) request.getParameter("sharerpassword");
            dbconnect db = dbconnect.dbconnectref();
            if (!db.update_session(sessionid)) {
                response.setStatus(response.SC_FORBIDDEN);
            } else {
                String userid = db.check_password(sessionid, sharerpassword);
                if (userid.equals(null) || userid.equals("")) {
                    response.setStatus(response.SC_FORBIDDEN);
                } else {
                    String sharedtoid = db.check_userexist(sharetousername);
                    if (sharedtoid.equals("null") || sharedtoid.equals("")) {
                        response.setStatus(response.SC_CONFLICT);
                    } else {
                        if (db.share(fileid, sharedtoid, userid)) {
                            response.setStatus(response.SC_ACCEPTED);
                        } else {
                            response.setStatus(response.SC_NOT_MODIFIED);
                        }
                    }
                }
            }
        } else {
            response.setStatus(response.SC_FORBIDDEN);
        }
    }
}
