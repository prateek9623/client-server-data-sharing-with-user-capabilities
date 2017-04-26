package client;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.fontawesome.AwesomeIcon;
import de.jensd.fx.fontawesome.Icon;
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
import javafx.animation.FadeTransition;
import javafx.application.Platform;
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
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author prate
 */
public class RegistersceneController implements Initializable {
    private Connect conn = Connect.getInstance();
    @FXML
    private ToggleGroup gender;
   @FXML
    private Label main_alert;
    @FXML
    private ProgressIndicator passscore;
    boolean phone_status = false;
    boolean email_status = false;
    boolean username_status = false;
    boolean password_status = false;
    private RequiredFieldValidator passwordvalidator;
    @FXML
    private JFXTextField firstName;
    @FXML
    private JFXTextField lastName;
    @FXML
    private JFXTextField email;
    @FXML
    private JFXTextField userName;
    @FXML
    private JFXTextField phone;
    @FXML
    private JFXPasswordField passWord;
    @FXML
    private JFXDatePicker dob;
    @FXML
    private JFXRadioButton male;
    @FXML
    private JFXRadioButton female;
    @FXML
    private VBox registerbox;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        male.setUserData("Male");
        female.setUserData("Female");
        RequiredFieldValidator phonevalid = new RequiredFieldValidator(){
            @Override
            protected void eval(){
                if(srcControl.get()instanceof TextInputControl){
                    TextInputControl textfield = (TextInputControl) srcControl.get();
                    if(textfield.getText()==null||textfield.getText().equals("")){
                        setMessage("Input is required");
                        phone_status= false;
                        hasErrors.set(true);
                    }else if(textfield.getText().length()<10){
                        phone_status = false;
                        setMessage("Atleast 10 digits are needed");
                        hasErrors.set(true);
                    }else{
                        phone_status=true;
                        hasErrors.set(false);
                    }
                }
            }
        };
        phonevalid.setIcon(new Icon(AwesomeIcon.WARNING, "20", ";", "error"));
        phone.getValidators().add(phonevalid);
        phone.focusedProperty().addListener((observable, oldValue, newValue) -> {
        if(!newValue){
            phone.validate();
        }else{
            phone.resetValidation();
        }
        });
        phone.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if (newValue.intValue() > oldValue.intValue()) {
                char ch = phone.getText().charAt(oldValue.intValue());
                // Check if the new character is the number or other's
                if (!(ch >= '0' && ch <= '9')) {
                    // if it's not number then just setText to previous one
                    phone.setText(phone.getText().substring(0, phone.getText().length() - 1));
                }
            }
        });
        RequiredFieldValidator emailvalidator = new RequiredFieldValidator(){
            @Override
            protected void eval(){
                if(srcControl.get()instanceof TextInputControl){
                    TextInputControl textfield = (TextInputControl) srcControl.get();
                    String regex = "^(.+)@(.+).(.+)$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(textfield.getText());
                    if(textfield.getText()==null||textfield.getText().equals("")){
                        setMessage("Input is required");
                        email_status= false;
                        hasErrors.set(true);
                    }else if(!matcher.matches()){
                        email_status = false;
                        setMessage("Invalid email id.");
                        hasErrors.set(true);
                    }else{
                        email_status=true;
                        hasErrors.set(false);
                    }
                }
            }
        };
        emailvalidator.setIcon(new Icon(AwesomeIcon.WARNING, "20", ";", "error"));
        email.getValidators().add(emailvalidator);
        email.focusedProperty().addListener((observable, oldValue, newValue) -> {
        if(!newValue){
            email.validate();
        }else{
            email.resetValidation();
        }
        });
        RequiredFieldValidator usernamevalidator = new RequiredFieldValidator(){
            @Override
            protected void eval(){
                if(srcControl.get()instanceof TextInputControl){
                    TextInputControl textfield = (TextInputControl) srcControl.get();
                    String regex = "^[a-z0-9_-]{3,15}$";
                    Pattern pattern = Pattern.compile(regex);
                    Matcher matcher = pattern.matcher(textfield.getText());
                    if(textfield.getText()==null||textfield.getText().equals("")){
                        setMessage("Input is required");
                        username_status= false;
                        hasErrors.set(true);
                    }else if(!matcher.matches()){
                        username_status = false;
                        setMessage("Invalid username.");
                        hasErrors.set(true);
                    }else{
                        username_status=true;
                        hasErrors.set(false);
                    }
                }
            }
        };
        usernamevalidator.setIcon(new Icon(AwesomeIcon.WARNING, "20", ";", "error"));
        userName.getValidators().add(usernamevalidator);
        userName.focusedProperty().addListener((observable, oldValue, newValue) -> {
        if(!newValue){
            userName.validate();
        }else{
            userName.resetValidation();
        }
        });
        passwordvalidator = new RequiredFieldValidator(){
            @Override
            protected void eval(){
                if(srcControl.get()instanceof TextInputControl){
                    TextInputControl textfield = (TextInputControl) srcControl.get();
                    if(textfield.getText()==null||textfield.getText().equals("")){
                        setMessage("Input is required");
                        phone_status= false;
                        hasErrors.set(true);
                    }else if(!password_status){
                        hasErrors.set(true);
                    }else{
                        username_status=true;
                        hasErrors.set(false);
                    }
                }
            }
        };
        passwordvalidator.setIcon(new Icon(AwesomeIcon.WARNING, "20", ";", "error"));
        passWord.getValidators().add(passwordvalidator);
        passWord.focusedProperty().addListener((observable, oldValue, newValue) -> {
        if(!newValue){
            passWord.validate();
        }else{
            passWord.resetValidation();
        }
        });
    }

    private int validatePassword() {
        int pass_score = 100;
        String usertext = userName.getText();
        String passtext = passWord.getText();
        boolean valid = true;
        if (passtext.length() > 15 || passtext.length() < 8) {
            passwordvalidator.setMessage("Password should be in range of 8 to 22.");
            pass_score -= 25;
            valid = false;
        }
        if (passtext.contains(usertext.trim())) {
            valid = false;
            passwordvalidator.setMessage("Password should not contain username.");
        }
//        String specialChars = "(.*[.,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (!Pattern.compile("[$&+,:;=?@#|]").matcher(passtext).find()) {
            passwordvalidator.setMessage("Password should contain atleast one special character");
            valid = false;
            pass_score -= 15;
        }
        if (!passtext.matches(".*\\d+.*")) {
            passwordvalidator.setMessage("Password should contain atleast one number.");
            valid = false;
            pass_score -= 15;
        }
        if (!passtext.matches("(.*[A-Z].*)")) {
            passwordvalidator.setMessage("Password should contain atleast one upper case alphabet");
            valid = false;
            pass_score -= 15;
        }
        if (!passtext.matches("(.*[a-z].*)")) {
            passwordvalidator.setMessage("Password should contain atleast one lower case alphabet");
            valid = false;
            pass_score -= 15;
        }
        if (valid) {
            passwordvalidator.setMessage("");
            pass_score = 100;
            password_status = true;
        } else {
            password_status = false;
        }
        return pass_score;
    }

    @FXML
    private void createAccount(ActionEvent event) throws IOException {
        if (!username_status || lastName.getText().trim().isEmpty() || !email_status || firstName.getText().trim().isEmpty() || phone.getText().trim().isEmpty() || !password_status) {
            main_alert.setText("Please fill all the fields correctly");
        } else {
            main_alert.setText("");
            int response = conn.register(firstName.getText(), lastName.getText(), email.getText(), phone.getText(), dob.getValue().toString(), gender.getSelectedToggle().getUserData().toString(), userName.getText(), passWord.getText());
            if (response==HttpStatus.SC_ACCEPTED) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/loginscene.fxml"));
                Parent register = loader.load();
                Scene scene = new Scene(register);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.hide();
                stage.setScene(scene);
                Platform.runLater(() -> {
                        Notifications.create()
                                .title("Information")
                                .text("Registration Successful.").hideAfter(Duration.seconds(2))
                                .showInformation();
                    });
                stage.show();
            } else {
                Platform.runLater(() -> {
                        Notifications.create()
                                .title("Information")
                                .text("Registration Unsuccessful.").hideAfter(Duration.seconds(2))
                                .showInformation();
                    });
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        VBox loginbox = new FXMLLoader(getClass().getResource("/resources/fxml/loginscene.fxml")).load();
        FadeTransition ft = new FadeTransition(Duration.millis(1000), registerbox);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        FadeTransition ft1 = new FadeTransition(Duration.millis(1000), loginbox);
        ft1.setFromValue(0.0);
        ft1.setToValue(1.0);
        ft.play();
        ft1.play();
    }

    @FXML
    private void passwordverification(KeyEvent event) {
        passscore.setProgress((double) validatePassword() / 100);
    }

    

}
