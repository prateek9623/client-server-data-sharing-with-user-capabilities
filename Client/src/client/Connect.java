/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 *
 * @author Prateek
 */
public class Connect {
    private static Connect conn = new Connect();
    private HttpClient httpclient = HttpClients.createDefault();
    private CookieStore cookieStore = new BasicCookieStore();
    private HttpContext httpContext = new BasicHttpContext();
    private Connect(){
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
    }
    public static Connect getInstance(){
        return conn;
    }

    public HttpClient getHttpclient() {
        return httpclient;
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }
    public String getResponse(HttpPost httppost){
        String sid;
        StringBuilder sb = new StringBuilder();
        try{
            HttpResponse response = httpclient.execute(httppost,httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream instream = entity.getContent()) {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(instream));
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        sid = sb.toString();
        return sid;
    }
}
