package client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

/**
 * FXML Controller class
 *
 * @author prate
 */
public class RegistersceneController implements Initializable {

    @FXML
    private TextField frontName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private TextField userName;
    @FXML
    private TextField phone;
    @FXML
    private PasswordField passWord;
    @FXML
    private DatePicker dob;
    @FXML
    private ToggleGroup gender;
    @FXML
    private Label pass_vali;
    @FXML
    private Label user_vali;
    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    @FXML
    private Label main_alert;
    @FXML
    private Label email_alert;
    @FXML
    private Label no_vali;
    @FXML
    private ProgressIndicator passscore;
    @FXML
    private Label dob_alert;
    boolean phone_status = false;
    @FXML
    private RadioButton male;
    @FXML
    private RadioButton female;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        male.setUserData("Male");
        female.setUserData("Female");
        phone.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                char ch = phone.getText().charAt(oldValue.intValue());
                // Check if the new character is the number or other's
                if (!(ch >= '0' && ch <= '9')) {
                    // if it's not number then just setText to previous one
                    phone.setText(phone.getText().substring(0, phone.getText().length() - 1));
                }
            }
            if (phone.getText().length() < 10) {
                phone_status = false;
                no_vali.setText("Very short number");
            } else {
                phone_status = true;
                no_vali.setText("");

            }
        });
    }

    boolean email_status = false;

    @FXML
    private void emailverification(KeyEvent event) {
        String regex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email.getText());
        if (!matcher.matches()) {
            email_alert.setText("Invalid email id.");
            email_status = false;
        } else {
            email_alert.setText("");
            email_status = true;
        }
    }

    boolean username_status = false;

    @FXML
    private void usernameverification(KeyEvent event) {
        String usertext = userName.getText();
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(usertext);
        if (!matcher.matches()) {
            user_vali.setText("Invalid Username");
            username_status = false;
        } else {
            user_vali.setText("");
            username_status = true;
        }
    }

    boolean password_status = false;

    @FXML
    private void passwordverification(KeyEvent event) {
        passscore.setProgress((double) validatePassword() / 100);
    }

    private int validatePassword() {
        int pass_score = 100;
        String usertext = userName.getText();
        String passtext = passWord.getText();
        boolean valid = true;
        if (passtext.length() > 15 || passtext.length() < 8) {
            pass_vali.setText("Password should be in range of 8 to 22.");
            pass_score -= 25;
            valid = false;
        }
        if (passtext.contains(usertext.trim())) {
            valid = false;
            pass_vali.setText("Password should not containg username.");
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!passtext.matches(upperCaseChars)) {
            pass_vali.setText("Password should contain atleast one upper case alphabet");
            valid = false;
            pass_score -= 15;
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!passtext.matches(lowerCaseChars)) {
            pass_vali.setText("Password should contain atleast one lower case alphabet");
            valid = false;
            pass_score -= 15;
        }
        String numbers = "(.*[0-9].*)";
        if (!passtext.matches(numbers)) {
            pass_vali.setText("Password should contain atleast one number.");
            valid = false;
            pass_score -= 15;
        }
        String specialChars = "(.*[.,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (!passtext.matches(specialChars)) {
            pass_vali.setText("Password should contain atleast one special character");
            valid = false;
            pass_score -= 15;
        }
        if (valid) {
            pass_vali.setText("");
            pass_score = 100;
            password_status = true;
        } else {
            password_status = false;
        }
        return pass_score;
    }

    @FXML
    private void createAccount(ActionEvent event) throws IOException {
        if (!username_status || lastName.getText().trim().isEmpty() || !email_status || frontName.getText().trim().isEmpty() || phone.getText().trim().isEmpty() || !password_status) {
            main_alert.setText("Please fill all the fields correctly");
        } else {
            main_alert.setText("");
            System.out.println("name: " + frontName.getText() + " " + lastName.getText());
            System.out.println("Email: " + email.getText());
            System.out.println("username: " + userName.getText());
            System.out.println("contact no.: " + phone.getText());
            System.out.println("DOB:" + dob.getValue().toString());
            System.out.println("pass:" + passWord.getText());
            System.out.println("Gender: " + gender.getSelectedToggle().getUserData().toString());
            String response = register(frontName.getText(), lastName.getText(), email.getText(), phone.getText(), dob.getValue().toString(), gender.getSelectedToggle().getUserData().toString(), userName.getText(), passWord.getText());
            if (response.equals("SUCCESS")) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("loginscene.fxml"));
                Parent register = loader.load();
                Scene scene = new Scene(register);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.hide();
                stage.setScene(scene);
                LoginsceneController controller = loader.getController();
                controller.set_status("Registration Successful");
                        
                stage.show();
                
            } else {
                main_alert.setText("Registration failed");
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginscene.fxml"));
        Parent register = loader.load();
        Scene scene = new Scene(register);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.hide();
        stage.setScene(scene);
        stage.show();
    }

    private String register(String fname, String lname, String email, String phone, String dob, String gender, String user, String pass) {
        String sid;
        StringBuilder sb = new StringBuilder();
        Connect connect = Connect.getInstance();
        HttpPost httppost = new HttpPost("http://localhost:8080/Server/register");
        try {
            List<NameValuePair> params = new ArrayList<>(2);
            params.add(new BasicNameValuePair("username", user));
            params.add(new BasicNameValuePair("password", pass));
            params.add(new BasicNameValuePair("fname", fname));
            params.add(new BasicNameValuePair("lname", lname));
            params.add(new BasicNameValuePair("phone", phone));
            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("gender", gender));
            params.add(new BasicNameValuePair("dob", dob));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RegistersceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        sid = connect.getResponse(httppost);
        return sid;
    }

}
