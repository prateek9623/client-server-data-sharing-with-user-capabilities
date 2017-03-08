package Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class LoginsceneController implements Initializable {

    @FXML
    private Label uservali;
    @FXML
    private TextField username;
    @FXML
    private Label passvali;
    @FXML
    private PasswordField pass;
    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    private boolean user_status = false;
    private boolean pass_status = false;
    private String sessionID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void login(ActionEvent event) throws IOException {
        if (username.getText().isEmpty() || pass.getText().isEmpty()) {
            uservali.setText("Please fill all the required details");
        } else if (user_status && pass_status || true) {
            sessionID = authorize(username.getText().trim(), pass.getText().trim());
            if (sessionID == null) {
                uservali.setText("Invalid username or password");
            } else {
                uservali.setText("Login Successful");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("mainscene.fxml"));
                Parent register = loader.load();
                Scene scene = new Scene(register);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.hide();
                stage.setScene(scene);
                stage.setMinHeight(720);
                stage.setMinWidth(1280);
                stage.show();
                MainsceneController controller = loader.getController();
                controller.passSessionId(sessionID);
            }
        }
    }

    @FXML
    private void validateUsername(KeyEvent event) {
        String usertext = username.getText();
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(usertext);
        if (!matcher.matches()) {
            uservali.setText("Invalid Username");
            user_status = false;
        } else {
            uservali.setText("");
            user_status = true;
        }
    }

    @FXML
    private void validatePassword(KeyEvent event) {
        String usertext = username.getText();
        String passtext = pass.getText();
        boolean valid = true;
        if (passtext.length() > 15 || passtext.length() < 8) {
            passvali.setText("Password should be less than 15 and more than 8 characters in length.");
            valid = false;
        } else {
            if (passtext.contains(usertext)) {
                passvali.setText("Password Should not be same as user name");
                valid = false;
            }
            String upperCaseChars = "(.*[A-Z].*)";
            if (!passtext.matches(upperCaseChars)) {
                passvali.setText("Password should contain atleast one upper case alphabet");
                valid = false;
            }
            String lowerCaseChars = "(.*[a-z].*)";
            if (!passtext.matches(lowerCaseChars)) {
                passvali.setText("Password should contain atleast one lower case alphabet");
                valid = false;
            }
            String numbers = "(.*[0-9].*)";
            if (!passtext.matches(numbers)) {
                passvali.setText("Password should contain atleast one number.");
                valid = false;
            }
            String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
            if (!passtext.matches(specialChars)) {
                passvali.setText("Password should contain atleast one special character");
                valid = false;
            }
        }
        if (valid) {
            passvali.setText("");
            pass_status = true;
        } else {
            pass_status = false;
        }
    }

    @FXML
    private void register(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("registerscene.fxml"));
        Parent register = loader.load();
        Scene scene = new Scene(register);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }

    private String authorize(String user, String pass) {
        String sid;
        StringBuilder sb = new StringBuilder();
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("http://localhost:8084/server/login");
            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("username", user));
            params.add(new BasicNameValuePair("password", pass));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
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
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(LoginsceneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LoginsceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        sid = sb.toString();
        return sid;
    }
    public void set_registration_status(){
        uservali.setText("Registration Successful");
    }
}
