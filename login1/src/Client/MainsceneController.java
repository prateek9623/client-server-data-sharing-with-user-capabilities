package Client;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

public class MainsceneController implements Initializable {

    private HttpClient httpClient = HttpClients.createDefault();
    private HttpPost sessionUpdate = new HttpPost("htpp://localhost:8084/server/updatesession");
    private List<NameValuePair> Sessionid = new ArrayList<>(1);
    private Service<Void> backgroundSessionThread;
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
        try {
            sessionUpdate.setEntity(new UrlEncodedFormEntity(Sessionid,"UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(MainsceneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        backgroundSessionThread = new Service<Void>() {
            @Override
            protected Task<Void> createTask() {
                while(true){
                    try {
                        httpClient.execute(sessionUpdate);
                    } catch (IOException ex) {
                        System.out.println("Couldn't update session");
                    }
                    try {
                        Thread.sleep(30*60*1000);
                    } catch (InterruptedException ex) {
                        System.out.println("Sleep update session error");
                    }
                }
            }
        };
        backgroundSessionThread.restart();
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
