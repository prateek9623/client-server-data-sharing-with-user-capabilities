package client;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;

public class MainsceneController implements Initializable {

    Connect connect = Connect.getInstance();
    private final HttpPost sessionUpdate = new HttpPost("http://localhost:8080/Server/updatesession");
    private List<NameValuePair> Sessionid = new ArrayList<>(1);
    @FXML
    private BorderPane toppane;
    @FXML
    private FlowPane panel1;
    @FXML
    private Button upload;
    private File file;
    @FXML
    private Pane user_display;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        String response = connect.getResponse(sessionUpdate);
        if(response.equalsIgnoreCase("INVALID")){
            System.out.println("INVALID LOGIN");
        }else{
            if(response.equalsIgnoreCase("FAILED")){
                System.out.println("Session Expired");
            }else{
                System.out.println("Welcome "+response);
            }
        }
    }    

    @FXML
    private void upload_file(ActionEvent event) {
        FileChooser filechooser = new FileChooser();
        file = filechooser.showOpenDialog(null);
        System.out.println(file.getPath());
    }

    void passSessionId(String sessionID) {
        Sessionid.add(new BasicNameValuePair("sessionid", sessionID));
    }    
}
