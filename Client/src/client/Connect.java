package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class Connect {

    private static final Connect conn = new Connect();
    private final HttpClient httpclient = HttpClients.createDefault();
    private final CookieStore cookieStore = new BasicCookieStore();
    private final HttpContext httpContext = new BasicHttpContext();
    private final HttpPost httpRegister = new HttpPost("http://localhost:8080/Server/register");
    private final HttpPost httpLogin = new HttpPost("http://localhost:8080/Server/login");
    private final HttpPost filelist = new HttpPost("http://localhost:8080/Server/getfilelist");
    private final HttpPost httpLogout = new HttpPost("http://localhost:8080/Server/logout");
    private final HttpPost filedecrypt = new HttpPost("http://localhost:8080/Server/decrypt");
    private final HttpPost filedelete = new HttpPost("http://localhost:8080/Server/delete");
    private final HttpPost fileencrypt = new HttpPost("http://localhost:8080/Server/encrypt");
    private final HttpPost filepredel = new HttpPost("http://localhost:8080/Server/predelete");
    private final HttpPost fileremove = new HttpPost("http://localhost:8080/Server/remove");
    private final HttpPost filerename = new HttpPost("http://localhost:8080/Server/rename");
    private final HttpPost filerestore = new HttpPost("http://localhost:8080/Server/restore");
    private final HttpPost fileshare = new HttpPost("http://localhost:8080/Server/share");
    private final HttpPost sessionUpdate = new HttpPost("http://localhost:8080/Server/updatesession");

    private Connect() {
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);
    }

    public static Connect getInstance() {
        return conn;
    }

    public HttpClient getHttpclient() {
        return httpclient;
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }
    private String sessionid;
    private String username;

    public String getSessionid() {
        return sessionid;
    }

    public int authorize(String user, String pass) {
        StringBuilder sb = new StringBuilder();
        try {
            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("username", user));
            params.add(new BasicNameValuePair("password", pass));
            httpLogin.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httpLogin, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                if (response.getStatusLine().getStatusCode() == HttpStatus.SC_ACCEPTED) {
                    try (InputStream instream = entity.getContent()) {
                        BufferedReader bReader = new BufferedReader(new InputStreamReader(instream));
                        String line = null;
                        while ((line = bReader.readLine()) != null) {
                            sb.append(line);
                        }
                    }
                    sessionid = sb.toString();
                }
                return response.getStatusLine().getStatusCode();
            } else {
                return HttpStatus.SC_EXPECTATION_FAILED;
            }
        } catch (Exception ex) {
            return HttpStatus.SC_FAILED_DEPENDENCY;
        }

    }

    public int register(String fname, String lname, String email, String phone, String dob, String gender, String user, String pass) {
        StringBuilder sb = new StringBuilder();
        try {
            List<NameValuePair> params = new ArrayList<>(8);
            params.add(new BasicNameValuePair("username", user));
            params.add(new BasicNameValuePair("password", pass));
            params.add(new BasicNameValuePair("fname", fname));
            params.add(new BasicNameValuePair("lname", lname));
            params.add(new BasicNameValuePair("phone", phone));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("gender", gender));
            params.add(new BasicNameValuePair("dob", dob));
            httpRegister.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httpRegister, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                return response.getStatusLine().getStatusCode();
            } else {
                return HttpStatus.SC_EXPECTATION_FAILED;
            }
        } catch (IOException ex) {
        }
        return HttpStatus.SC_FAILED_DEPENDENCY;
    }

    public int updateSession() {
        try {
            HttpResponse response = httpclient.execute(sessionUpdate, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                StringBuilder sb = new StringBuilder();
                try (InputStream instream = entity.getContent()) {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(instream));
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                }
                username = sb.toString();
                return response.getStatusLine().getStatusCode();
            } else {
                return HttpStatus.SC_EXPECTATION_FAILED;
            }
        } catch (IOException ex) {
        }
        return HttpStatus.SC_FAILED_DEPENDENCY;
    }

    String getusername() {
        return username;
    }

    public boolean logout() {
        try {
            if (httpclient.execute(httpLogout, httpContext).getEntity() != null) {
                return true;
            }
        } catch (IOException ex) {

        }
        return false;
    }

    int getFileList(StringBuilder sb) {
        int x = 808;
        try {
            HttpResponse response = httpclient.execute(filelist, httpContext);
            HttpEntity entity = response.getEntity();
            x = response.getStatusLine().getStatusCode();
            if (x == HttpStatus.SC_ACCEPTED) {
                try (InputStream instream = entity.getContent()) {
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(instream));
                    String line = null;
                    while ((line = bReader.readLine()) != null) {
                        sb.append(line);
                    }
                }
            }
        } catch (IOException ex) {
        }
        return x;
    }

    int fileRename(String newname, String file_id) {
        int status = 808;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fileid", file_id));
        params.add(new BasicNameValuePair("newname", newname));
        try {
            filerename.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(filerename, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                status = response.getStatusLine().getStatusCode();
            }
        } catch (Exception ex) {
        }
        return status;
    }

    int fileDecrypt(String file_id, String decryptpass, String pass) {
        int status = 808;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fileid", file_id));
        params.add(new BasicNameValuePair("oldpass", decryptpass));
        params.add(new BasicNameValuePair("userpass", pass));
        try {
            filedecrypt.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(filedecrypt, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                status = response.getStatusLine().getStatusCode();

            }
        } catch (IOException ex) {
        }
        return status;
    }

    int filePermadelete(String file_id, String userpass) {
        int status = 808;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("deletefile", file_id));
        params.add(new BasicNameValuePair("password", userpass));
        try {
            filedelete.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(filedelete, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                status = response.getStatusLine().getStatusCode();
            }
        } catch (IOException ex) {
            Logger.getLogger(Connect.class.getName()).log(Level.SEVERE, null, ex);
        }
        return status;
    }

    int fileEncrypt(String file_id, String encryptpass, String pass) {
        int status = 808;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fileid", file_id));
        params.add(new BasicNameValuePair("newencryptpass", encryptpass));
        params.add(new BasicNameValuePair("userpass", pass));
        try {
            fileencrypt.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(fileencrypt, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                status = response.getStatusLine().getStatusCode();
            }
        } catch (Exception ex) {
        }
        return status;
    }

    int filePredelete(String file_id) {
        int status = 808;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fileid", file_id));
        try {
            filepredel.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(filepredel, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                status = response.getStatusLine().getStatusCode();
            }
        } catch (Exception ex) {
        }
        return status;
    }

    int shareFile(String sharedtoid, String sharerpassword, String file_id) {
        int status = 808;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("sharedtoid", sharedtoid));
        params.add(new BasicNameValuePair("sharerpassword", sharerpassword));
        params.add(new BasicNameValuePair("fileid", file_id));
        try {
            fileshare.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(fileshare, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                status = response.getStatusLine().getStatusCode();
            }
        } catch (Exception ex) {
        }
        return status;
    }

    int unshareFile(String id, String pass) {
        int status = 808;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fileid", id));
        params.add(new BasicNameValuePair("password", pass));
        try {
            fileremove.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(fileremove, httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                status = response.getStatusLine().getStatusCode();
            }
        } catch (Exception ex) {
        }
        return status;
    }

    int restoreFile(String file_id) {
        int status = 808;
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("fileid",file_id));
        try {
            filerestore.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(filerestore,httpContext);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                status = response.getStatusLine().getStatusCode();
            }
        } catch (Exception ex) {
        }
        return status;
    }
}
