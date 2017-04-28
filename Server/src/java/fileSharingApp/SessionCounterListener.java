/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileSharingApp;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 *
 * @author Prateek
 */
public class SessionCounterListener implements HttpSessionListener {

    private static int totalActiveSessions;

    public static int getTotalActiveSession() {
        return totalActiveSessions;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        totalActiveSessions++;
        System.out.println("sessionCreated - add one session into counter");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        totalActiveSessions--;
        dbconnect db = dbconnect.dbconnectref();
        String sessionid = se.getSession().getId();
        db.delete_session(sessionid);
        System.out.println("sessionDestroyed - deduct one session from counter");
    }
}
