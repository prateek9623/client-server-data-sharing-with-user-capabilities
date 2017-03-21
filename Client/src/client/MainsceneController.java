package client;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import java.io.BufferedReader;
import java.io.File;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.controlsfx.control.Notifications;
import org.controlsfx.control.TaskProgressView;

public class MainsceneController implements Initializable {

    Connect connect = Connect.getInstance();
    private final HttpPost sessionUpdate = new HttpPost("http://localhost:8080/Server/updatesession");
    private final CloseableHttpClient client1 = HttpClients.createDefault();
    private final HttpPost filepost = new HttpPost("http://localhost:8080/Server/upload");
    private JFXButton settings = new JFXButton("Settings");
    private JFXButton logout = new JFXButton("Logout");
    private String Sessionid = null;
    private boolean optionpane1 = false;
    private JFXButton button1 = new JFXButton("button1");
    private JFXButton upload = new JFXButton("Upload");
    private File file;
    @FXML
    private JFXButton avatar;
    @FXML
    private SplitPane menudrawer;
    @FXML
    private Pane optionpane;
    @FXML
    private JFXHamburger slider_button;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String response = connect.getResponse(sessionUpdate);
        if(response.equalsIgnoreCase("INVALID")){
            System.out.println("INVALID LOGIN");
            return;
        }else{
            if(response.equalsIgnoreCase("FAILED")){
                System.out.println("Session Expired");
                return;
            }else{
                System.out.println("Welcome "+response);
            }
        }
        menudrawer.setDividerPositions(0);
        button1.getStyleClass().add("button-raised");
        upload.getStyleClass().add("button-raised");
        VBox optionList = new VBox(10);
        optionList.setPadding(new Insets(10));
        optionList.getChildren().addAll(button1, upload);
        optionList.setMinWidth(228);
        HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(slider_button);
        burgerTask.setRate(-1);
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.3), slider_button);
        TranslateTransition drawerTransition = new TranslateTransition();

        slider_button.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
            if (optionpane1 == true) {
                optionpane.getChildren().remove(optionList);
                menudrawer.setDividerPositions(0);
                transition.setByX(-200);
                optionpane1 = false;
            } else {
                optionpane.getChildren().add(optionList);
                menudrawer.setDividerPositions(230 / menudrawer.getWidth());
                optionpane1 = true;
                transition.setByX(200);
            }
            transition.play();
            burgerTask.setRate(burgerTask.getRate() * -1);
            burgerTask.play();
        });
        settings.getStyleClass().add("button-raised");
        logout.getStyleClass().add("button-raised");
        settings.setOnAction((e) -> {
            System.out.println("settings");
        });
        logout.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setContentText("Do you want to Logout");
            ButtonType cancel = new ButtonType("Cancel");
            ButtonType logout_button = new ButtonType("Logout");
            alert.getButtonTypes().setAll(cancel, logout_button);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == logout_button) {
                System.out.println("Logout");
            } else {
                System.out.println("Cancel");
            }
        });
        JFXListView popuplist = new JFXListView();
        popuplist.getItems().add(settings);
        popuplist.getItems().add(logout);
        JFXPopup popup = new JFXPopup(popuplist);
        popuplist.setMinSize(228, 125);
        avatar.setOnAction(e -> popup.show(avatar, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -50, 100));

        upload.setOnAction((ActionEvent e) -> {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Select upload files");
            filechooser.setInitialDirectory(new File(System.getProperty("user.home")));
            List<File> list = filechooser.showOpenMultipleDialog(upload.getScene().getWindow());
            if (!list.isEmpty()) {
                upload(list);
            }
        });
    }

    private void upload(List<File> list) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Task<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Task task = task(list.get(i));
            executorService.submit(task);
            tasks.add(task);
        }
        TaskProgressView<Task<Void>> view = new TaskProgressView<>();
        //view.setGraphicFactory(t -> new ImageView(new Image(getClass().getResourceAsStream("/icon.png"))));
        view.getTasks().addAll(tasks);

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Uploading");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(upload.getScene().getWindow());
        Scene scene = new Scene(view);
        dialogStage.setScene(scene);
        dialogStage.setOnCloseRequest(event -> {
            executorService.shutdownNow();
            dialogStage.hide();
        });

        dialogStage.show();
        executorService.shutdown();
        new Thread(() -> {
            try {
                executorService.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                Platform.runLater(dialogStage::hide);
            }
        }).start();
    }

    public Task<Void> task(File file) {
        return new Task<Void>() {
            @Override
            protected Void call()
                    throws InterruptedException, UnsupportedEncodingException, IOException {
                try {
                    Thread.sleep(ThreadLocalRandom.current().nextInt(200, 2000));
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                }
                Connect connect = Connect.getInstance();
                updateMessage("Finding files . . .");
                updateProgress(0, 100);
                FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.addPart("file", fileBody);
                builder.addPart("sessionid",new StringBody(Sessionid, ContentType.DEFAULT_TEXT));
                HttpEntity entity = builder.build();
                updateMessage(file.getName());
                ProgressListener listener = (float percentage) -> {
                    updateProgress(percentage, 100);
                };
                filepost.setEntity(new ProgressEntityWrapper(entity, listener));
                try {
                    CloseableHttpResponse response = client1.execute(filepost);
                    HttpEntity responsEntity = response.getEntity();
                    InputStream instream = entity.getContent();
                    BufferedReader bReader = new BufferedReader(new InputStreamReader(instream));
                    if("SUCCESS".equals(bReader.readLine())){
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text(file.getName() + " Uploaded").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        return null;
                    }else{
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File not uploaded "+file.getName()).hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        return null;
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                    return null;
                }
                
            }
        };
    }

    private static interface ProgressListener {

        void progress(float percentage);
    }

    private static class ProgressEntityWrapper extends HttpEntityWrapper {

        private ProgressListener listener;

        public ProgressEntityWrapper(HttpEntity entity,
                ProgressListener listener) {
            super(entity);
            this.listener = listener;
        }

        @Override
        public void writeTo(OutputStream outstream) throws IOException {
            super.writeTo(new CountingOutputStream(outstream,
                    listener, getContentLength()));
        }
    }

    private static class CountingOutputStream extends FilterOutputStream {

        private ProgressListener listener;
        private long transferred;
        private long totalBytes;

        public CountingOutputStream(
                OutputStream out, ProgressListener listener, long totalBytes) {
            super(out);
            this.listener = listener;
            transferred = 0;
            this.totalBytes = totalBytes;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            transferred += len;
            listener.progress(getCurrentProgress());
        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
            transferred++;
            listener.progress(getCurrentProgress());
        }

        private float getCurrentProgress() {
            return ((float) transferred / totalBytes) * 100;
        }
    }


    void passSessionId(String sessionID) {
        Sessionid=sessionID;
    }    
}
