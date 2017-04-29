package client;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import de.jensd.fx.fontawesome.AwesomeIcon;
import de.jensd.fx.fontawesome.Icon;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.http.HttpStatus;
import org.controlsfx.control.MaskerPane;
import org.controlsfx.control.Notifications;

public class LoginsceneController implements Initializable {
    private final Connect connect = Connect.getInstance();
    @FXML
    private JFXTextField username;
    @FXML
    private ProgressIndicator passscore;
    boolean phone_status = false;
    boolean email_status = false;
    boolean username_status = false;
    boolean password_status = false;
    private RequiredFieldValidator passwordvalidator;
    private RequiredFieldValidator usernameVali;
    private RequiredFieldValidator passVali;
    private static final String USERNAME_PATTERN = "^[a-z0-9_-]{3,15}$";
    private boolean user_status = false;
    private boolean pass_status = false;
    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/mainscene.fxml"));
    @FXML
    private JFXPasswordField pass;
    @FXML
    private Label message;
    @FXML
    private VBox loginbox;
    @FXML
    private VBox registerbox;
    @FXML
    private StackPane container;
    @FXML
    private Label main_alert;
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
    private ToggleGroup gender;
    @FXML
    private JFXRadioButton female;
    @FXML
    private MaskerPane masker;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        usernameVali = new RequiredFieldValidator();
        usernameVali.setIcon(new Icon(AwesomeIcon.WARNING, "20", ";", "error"));
        username.getValidators().add(usernameVali);
        username.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                usernameVali.setMessage("Input is required.");
                username.validate();
            }
        });
        passVali = new RequiredFieldValidator();
        passVali.setIcon(new Icon(AwesomeIcon.WARNING, "20", ";", "error"));
        pass.getValidators().add(passVali);
        pass.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                if (pass.getText().isEmpty()) {
                    passVali.setMessage("Input is required.");
                    pass.validate();
                }
            }
        });
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

    private void validatefields() {
        String usertext = username.getText();
        Pattern pattern = Pattern.compile(USERNAME_PATTERN);
        Matcher matcher = pattern.matcher(usertext);
        if (!matcher.matches()) {
            message.setText("Invalid Username");
            message.setVisible(true);
            user_status = false;
        } else {
            message.setVisible(false);
            user_status = true;
            String passtext = pass.getText();
            boolean valid = true;
            if (passtext.length() > 15 || passtext.length() < 8) {
                message.setText("Password should be less than 15 and more than 8 characters in length.");
                valid = false;
            } else {
                if (passtext.contains(usertext)) {
                    message.setText("Password Should not be same as user name");
                    valid = false;
                }
                String upperCaseChars = "(.*[A-Z].*)";
                if (!passtext.matches(upperCaseChars)) {
                    message.setText("Password should contain atleast one upper case alphabet");
                    valid = false;
                }
                String lowerCaseChars = "(.*[a-z].*)";
                if (!passtext.matches(lowerCaseChars)) {
                    message.setText("Password should contain atleast one lower case alphabet");
                    valid = false;
                }
                String numbers = "(.*[0-9].*)";
                if (!passtext.matches(numbers)) {
                    message.setText("Password should contain atleast one number.");
                    valid = false;
                }
                String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
                if (!passtext.matches(specialChars)) {
                    message.setText("Password should contain atleast one special character");
                    valid = false;
                }
            }
            if (valid) {
                message.setVisible(false);
                pass_status = true;
            } else {
                message.setVisible(true);
                pass_status = false;
            }
        }
    }

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
            passwordvalidator.setMessage("Password should be in range of 8 to 22.");
            pass_score -= 25;
            valid = false;
        }
        if (passtext.contains(usertext.trim())) {
            valid = false;
            passwordvalidator.setMessage("Password should not contain username.");
        }
//        String specialChars = "(.*[.,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";
        if (!Pattern.compile("[$&+,:;=?@#|.]").matcher(passtext).find()) {
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
    private void register(ActionEvent event) throws IOException {
        registerbox.setVisible(true);
        FadeTransition ft = new FadeTransition(Duration.millis(1000), loginbox);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        FadeTransition ft1 = new FadeTransition(Duration.millis(1000), registerbox);
        ft1.setFromValue(0.0);
        ft1.setToValue(1.0);
        ft.play();
        ft1.play();
        registerbox.toFront();
    }
    
    @FXML
    private void cancel(ActionEvent event) {
        FadeTransition ft = new FadeTransition(Duration.millis(1000), registerbox);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        FadeTransition ft1 = new FadeTransition(Duration.millis(1000), loginbox);
        ft1.setFromValue(0.0);
        ft1.setToValue(1.0);
        ft.play();
        ft1.play();
        loginbox.toFront();
    }

    @FXML
    private void createAccount(ActionEvent event) throws IOException {
        if (!username_status || lastName.getText().trim().isEmpty() || !email_status || firstName.getText().trim().isEmpty() || phone.getText().trim().isEmpty() || !password_status) {
            main_alert.setText("Please fill all the fields correctly");
        } else {
            main_alert.setText("");
            int response = connect.register(firstName.getText(), lastName.getText(), email.getText(), phone.getText(), dob.getValue().toString(), gender.getSelectedToggle().getUserData().toString(), userName.getText(), passWord.getText());
            if (response==HttpStatus.SC_ACCEPTED) {
                sidePaneNotification("Registration Successful.");
                FadeTransition ft = new FadeTransition(Duration.millis(1000), registerbox);
                ft.setFromValue(1.0);
                ft.setToValue(0.0);
                FadeTransition ft1 = new FadeTransition(Duration.millis(1000), loginbox);
                ft1.setFromValue(0.0);
                ft1.setToValue(1.0);
                ft.play();
                ft1.play();
                loginbox.toFront();
            } else {
                sidePaneNotification("Registration Unsuccessful.");
            }
        }
    }
    
    @FXML
    private void login(ActionEvent event) throws IOException {
        if (!username.getText().isEmpty() && !pass.getText().isEmpty()) {
            username.resetValidation();
            pass.resetValidation();
            validatefields();
            username.validate();
            pass.validate();
            if ((user_status && pass_status )|| true) {
                int status = connect.authorize(username.getText().trim(), pass.getText().trim());
                switch (status) {
                    case HttpStatus.SC_ACCEPTED:
                        sidePaneNotification("Login successfull.");
                        Parent main = loader.load();
                        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        Stage newStage = new Stage();
                        JFXDecorator decorator = new JFXDecorator(newStage, main);
                        decorator.customMaximizeProperty().setValue(false);
                        decorator.setCustomMaximize(true);
                        Scene scene =new Scene(decorator);
                        scene.getStylesheets().add(Client.class.getResource("/resources/css/jfoenix-fonts.css").toExternalForm());
                        scene.getStylesheets().add(Client.class.getResource("/resources/css/jfoenix-design.css").toExternalForm());
                        scene.getStylesheets().add(Client.class.getResource("/resources/css/jfoenix-main-demo.css").toExternalForm());
                        scene.getStylesheets().add(Client.class.getResource("/resources/css/treetableview.css").toExternalForm());
                        newStage.setScene(scene);
                        newStage.show();
                        stage.close();
                        message.setVisible(false);
                        break;
                    case HttpStatus.SC_NON_AUTHORITATIVE_INFORMATION:
                        sidePaneNotification("Invalid Username");
                        message.setText("Invalid Username.");
                        message.setVisible(true);
                        break;
                    case HttpStatus.SC_UNAUTHORIZED:
                        sidePaneNotification("Invalid Password");
                        message.setText("Invalid Password.");
                        message.setVisible(true);
                        break;
                    default:
                        sidePaneNotification("Check Connection");
                        break;
                }
            }
        } else {
            if (username.getText().isEmpty()) {
                usernameVali.setMessage("Input is required.");
                username.validate();
            }
            if (pass.getText().isEmpty()) {
                passVali.setMessage("Input is required.");
                pass.validate();
            }
        }
    }
    void haltexecution() {
        MainsceneController controller = loader.getController();
        try{
        controller.haltexecution();
        }catch(Exception e){
            Platform.exit();
        }
    }

    private void sidePaneNotification(String message) {
        Platform.runLater(() -> {
                        Notifications.create()
                                .title("Information")
                                .text(message).hideAfter(Duration.seconds(2))
                                .showInformation();
                    });
    }
}
