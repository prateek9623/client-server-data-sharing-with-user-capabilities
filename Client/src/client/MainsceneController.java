package client;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleNode;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.codehaus.jackson.map.ObjectMapper;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
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
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
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
    private final CloseableHttpClient client = HttpClients.createDefault();
    private final HttpPost filedownload = new HttpPost("http://localhost:8080/Server/download");
    private final HttpPost fileupload = new HttpPost("http://localhost:8080/Server/upload");
    private final HttpClient httpclient = HttpClients.createDefault();
    private HamburgerBackArrowBasicTransition burgerTask;
    private SplitPane toppane;
    @FXML
    private JFXButton avatar;
    @FXML
    private SplitPane menudrawer;
    @FXML
    private JFXHamburger slider_button;
    private JFXButton settings = new JFXButton("Settings");
    private JFXButton logout = new JFXButton("Logout");
    @FXML
    private Pane optionpane;
    @FXML
    private VBox optionpane1;
    @FXML
    private JFXButton myFiles;
    @FXML
    private JFXButton sharedWithMe;
    @FXML
    private JFXButton uploadButton;
    private Stage uploadStage = new Stage();
    private Stage downloadStage = new Stage();
    private TaskProgressView<Task<Void>> uploadtask = new TaskProgressView<>();
    private TaskProgressView<Task<Void>> downloadtask = new TaskProgressView<>();
    private Thread backThread;
    private File directory;
    private ObjectMapper mapper = new ObjectMapper();
    @FXML
    private HBox uploaddownload;
    private String sessionid;
    @FXML
    private SplitPane mfpane;
    private ToggleGroup mfbuttonGroup = new ToggleGroup();
    private ToggleGroup sfbuttonGroup = new ToggleGroup();
    private ToggleGroup binbuttonGroup = new ToggleGroup();
    @FXML
    private FlowPane myfiles;
    @FXML
    private SplitPane swmpane;
    @FXML
    private FlowPane sharedwithme;
    private boolean threadstatus = true;
    @FXML
    private JFXDialog sharedialog;
    @FXML
    private StackPane rootpane;
    @FXML
    private JFXTextField receiverid;
    @FXML
    private JFXPasswordField sharerPassowrd;
    @FXML
    private JFXDialog uploaddialog;
    @FXML
    private JFXButton uploadconfirm;
    @FXML
    private JFXTextArea filelocation;
    @FXML
    private JFXButton chooseFile;
    @FXML
    private JFXCheckBox checkEncrypt;
    @FXML
    private JFXPasswordField encryptpass;
    @FXML
    private SplitPane sharehistory;
    @FXML
    private SplitPane recycleview;
    @FXML
    private JFXDialog logoutdialog;
    @FXML
    private JFXButton logoutConfirmButton;
    private volatile ArrayList<file> fileList = new ArrayList<>();
    ObservableList<sharedToFileList> sharedFileList = FXCollections.observableArrayList();
    @FXML
    private JFXDialog downloaddialog;
    @FXML
    private JFXTextField downloc;
    @FXML
    private JFXPasswordField decryptpass;
    @FXML
    private JFXButton downloadConfirmbutton;
    @FXML
    private JFXButton downloadcancel;
    @FXML
    private JFXButton downlocbrowse;
    /*
        mfpopup - myfile right click popup
        mfepopup - myfile encrypt right click popup
        sfpopup - shared file popup
        binpopup - recyclebin popup
     */
    private JFXPopup mfpopup;
    private JFXPopup mfepopup;
    private JFXPopup sfpopup;
    private JFXPopup binpopup;
    @FXML
    private JFXButton shareconfirm;
    @FXML
    private FlowPane binpane;
    @FXML
    private JFXButton shareHistoryButton;
    @FXML
    private JFXButton binbutton;
    @FXML
    private JFXDialog deletedialog;
    @FXML
    private JFXPasswordField userpass;
    @FXML
    private JFXButton deleteConfirmbutton;
    @FXML
    private Label delefilename;
    @FXML
    private StackPane panestack;
    @FXML
    private JFXDialog renamedialog;
    @FXML
    private Label renamefilename;
    @FXML
    private JFXTextField newfileName;
    @FXML
    private JFXButton renameConfirmbutton;
    @FXML
    private JFXDialog encryptdialog;
    @FXML
    private Label encryptfilename;
    @FXML
    private JFXPasswordField newEncryptPass;
    @FXML
    private JFXPasswordField userpass2;
    @FXML
    private JFXButton encryptConfirmbutton;
    @FXML
    private Label decryptfilename;
    @FXML
    private JFXPasswordField decryptPass;
    @FXML
    private JFXPasswordField userpass3;
    @FXML
    private JFXButton decryptConfirmbutton;
    @FXML
    private JFXDialog decryptdialog;
    @FXML
    private Label fileextension;
    @FXML
    private BorderPane binproppane;
    @FXML
    private BorderPane sfproppane;
    @FXML
    private BorderPane mfproppane;
    @FXML
    private Label binfilename;
    @FXML
    private Label binfsize;
    @FXML
    private Label binstoredon;
    @FXML
    private Label binowner;
    @FXML
    private Label binshared;
    @FXML
    private Label sffilename;
    @FXML
    private Label sfsize;
    @FXML
    private Label sfstoredon;
    @FXML
    private Label sfowner;
    @FXML
    private Label mffilename;
    @FXML
    private Label mfsize;
    @FXML
    private Label mfstoredon;
    @FXML
    private Label mfowner;
    @FXML
    private Label mfshared;
    @FXML
    private JFXTreeTableView<sharedToFileList> sharehistoryTreeTable;
    @FXML
    private JFXTextField searchFile;
    @FXML
    private JFXTreeTableColumn<sharedToFileList, String> fileNameColumn;
    @FXML
    private JFXTreeTableColumn<sharedToFileList, String> fileSizeColumn;
    @FXML
    private JFXTreeTableColumn<sharedToFileList, String> sharedToColumn;
    @FXML
    private JFXTreeTableColumn<sharedToFileList, String> sharedOnColumn;
    @FXML
    private MenuItem resetgroup;
    @FXML
    private MenuItem groupByUsers;
    @FXML
    private MenuItem groupByFiles;
    @FXML
    private JFXButton unsharebutton;
    @FXML
    private JFXDialog unsharedialog;
    @FXML
    private JFXPasswordField userpass4;
    @FXML
    private Label unsharefilename;
    @FXML
    private JFXButton unshareConfirmbutton;
    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/fxml/loginscene.fxml"));

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int sessionstatus = connect.updateSession();
        if (sessionstatus != HttpStatus.SC_ACCEPTED) {
            if (sessionstatus == HttpStatus.SC_UNAUTHORIZED) {
                Platform.runLater(() -> {
                    Notifications.create()
                            .title("Information")
                            .text("Invalid access!!").hideAfter(Duration.seconds(2))
                            .showInformation();
                });
            } else if (sessionstatus == HttpStatus.SC_GATEWAY_TIMEOUT) {
                Platform.runLater(() -> {
                    Notifications.create()
                            .title("Information")
                            .text("Session Expired").hideAfter(Duration.seconds(2))
                            .showInformation();
                });
            }
            haltexecution();
        }
        sessionid = connect.getSessionid();
        avatar.setText(connect.getusername());
        toppane = mfpane;
        rightclickpopup();
        mfbuttonGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                mffilename.setText("");
                mfowner.setText("");
                mfshared.setText("");
                mfsize.setText("");
                mfstoredon.setText("");
            } else {
                int i = (int) newValue.getUserData();
                file file1 = fileList.get(i);
                mffilename.setText(file1.getFile_name());
                mfowner.setText(file1.getOwnerusername());
                mfshared.setText(file1.getShared());
                long size = Long.parseLong(file1.getFile_size());
                double k = size / 1024;
                double m = size / 1048576;
                double g = size / 1073741824;
                mfsize.setText(g > 1 ? g + "GB" : (m > 1 ? m + "MB" : (k > 1 ? k + "KB" : size + "bytes")));
                mfstoredon.setText(file1.getStored_on());
            }
        });
        sfbuttonGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                sffilename.setText("");
                sfowner.setText("");
                sfsize.setText("");
                sfstoredon.setText("");
            } else {
                int i = (int) newValue.getUserData();
                file file1 = fileList.get(i);
                sffilename.setText(file1.getFile_name());
                sfowner.setText(file1.getOwnerusername());
                long size = Long.parseLong(file1.getFile_size());
                double k = size / 1024;
                double m = size / 1048576;
                double g = size / 1073741824;
                sfsize.setText(g > 1 ? g + "GB" : (m > 1 ? m + "MB" : (k > 1 ? k + "KB" : size + "bytes")));
                sfstoredon.setText(file1.getStored_on());
            }
        });
        binbuttonGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                binfilename.setText("");
                binshared.setText("");
                binowner.setText("");
                binfsize.setText("");
                binstoredon.setText("");
            } else {
                int i = (int) newValue.getUserData();
                file file1 = fileList.get(i);
                binfilename.setText(file1.getFile_name());
                binowner.setText(file1.getOwnerusername());
                long size = Long.parseLong(file1.getFile_size());
                double k = size / 1024;
                double m = size / 1048576;
                double g = size / 1073741824;
                binshared.setText(file1.getShared());
                binfsize.setText(g > 1 ? g + "GB" : (m > 1 ? m + "MB" : (k > 1 ? k + "KB" : size + "bytes")));
                binstoredon.setText(file1.getStored_on());
            }
        });
        getfilelist();
        paintShareHistoryTabel();
        burgerTask = new HamburgerBackArrowBasicTransition(slider_button);
        burgerTask.setRate(-1);
        settings.getStyleClass().add("button-raised");
        logout.getStyleClass().add("button-raised");
        settings.setOnAction((e) -> {
            System.out.println("settings");
        });
        logout.setOnAction(e -> {
            logoutConfirmButton.setOnAction((event) -> {
                if (connect.logout()) {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("Information")
                                .text("Logout Success").hideAfter(Duration.seconds(2))
                                .showInformation();
                    });
                    logoutToLogin(event);
                } else {
                    Platform.runLater(() -> {
                        Notifications.create()
                                .title("Information")
                                .text("Logout Failed").hideAfter(Duration.seconds(2))
                                .showInformation();
                    });
                }
            });
            logoutdialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
            logoutdialog.maxHeight(100);
            logoutdialog.maxWidth(200);
            logoutdialog.show(rootpane);
        });
        VBox popuplist = new VBox();
        popuplist.getChildren().addAll(settings, logout);
        popuplist.setAlignment(Pos.CENTER);
        popuplist.setSpacing(10);
        popuplist.setMinSize(170, 100);
        JFXPopup popup = new JFXPopup(popuplist);
        avatar.setOnAction(e -> popup.show(avatar, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT, -50, 100));
        uploadStage.setTitle("Uploading");
        uploadStage.setScene(new Scene(uploadtask));
        JFXToggleNode uploadbutton = new JFXToggleNode();
        uploadbutton.setGraphic(new BorderPane(new ImageView("resources/Upload_32.png")));
        uploadbutton.setOnAction(e -> {
            if (uploadbutton.isSelected()) {
                uploadStage.show();
            } else {
                uploadStage.hide();
            }
        });
        uploadStage.setOnCloseRequest(event -> {
            uploadStage.hide();
            uploadbutton.setSelected(false);
        });
        downloadStage.setTitle("Downloading");
        downloadStage.setScene(new Scene(downloadtask));
        JFXToggleNode downloadbutton = new JFXToggleNode();
        downloadbutton.setGraphic(new BorderPane(new ImageView("resources/Download_32.png")));
        downloadbutton.setOnAction(e -> {
            if (downloadbutton.isSelected()) {
                downloadStage.show();
            } else {
                downloadStage.hide();
            }
        });
        downloadStage.setOnCloseRequest(event -> {
            downloadStage.hide();
            downloadbutton.setSelected(false);
        });
        uploaddownload.getChildren().addAll(uploadbutton, downloadbutton);
    }
//done

    private void getfilelist() {
        Task task = new Task<Void>() {
            @Override
            public Void call() throws Exception {
                while (true && threadstatus) {
                    Platform.runLater(() -> {
                        paintcontains();
                    });
                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException ex) {
                    }
                }
                return null;
            }
        };
        backThread = new Thread(task);
        backThread.setDaemon(true);
        backThread.start();
    }
//done

    private void paintcontains() {
        StringBuilder sb = new StringBuilder();
        int status = connect.getFileList(sb);
        if (status == HttpStatus.SC_ACCEPTED) {
            try {
                file[] files = mapper.readValue(sb.toString(), file[].class);
                fileList.clear();
                if (!myfiles.getChildren().isEmpty()) {
                    myfiles.getChildren().clear();
                    mfbuttonGroup.getToggles().clear();
                }
                if (!sharedwithme.getChildren().isEmpty()) {
                    sharedwithme.getChildren().clear();
                    sfbuttonGroup.getToggles().clear();
                }
                if (!binpane.getChildren().isEmpty()) {
                    binpane.getChildren().clear();
                    binbuttonGroup.getToggles().clear();
                }
                sharedFileList.clear();
                for (int i = 0; i < files.length;) {
                    fileList.add(files[i]);
                    if (!files[i].getDirectory().equals("sharedto")) {
                        paintbutton(i);
                    } else {
                        String x = files[i].getFile_id();
                        sharedFileList.add(new sharedToFileList(x, files[i].getFile_name() + "", files[i].getFile_size() + "", files[i].getStored_on() + "", files[i].getSharedto() + ""));
                    }
                    i++;
                }
            } catch (IOException ex) {
                Platform.runLater(() -> {
                    Notifications.create()
                            .title("Information")
                            .text("Connection Lost").hideAfter(Duration.seconds(2))
                            .showInformation();
                });
            }
        } else {
            Platform.runLater(() -> {
                Notifications.create()
                        .title("Information")
                        .text("Connection Lost").hideAfter(Duration.seconds(2))
                        .showInformation();
            });
        }
    }
//done

    private void paintbutton(int i) {
        String imageloc;
        String filename = fileList.get(i).getFile_name();
        if (filename.endsWith("pdf")) {
            imageloc = "resources/PDF_2_32.png";
        } else if (filename.endsWith("doc") || filename.endsWith("docx")) {
            imageloc = "resources/Word_32.png";
        } else if (filename.endsWith("xls") || filename.endsWith("xlsx") || filename.endsWith("gsheet")) {
            imageloc = "resources/XLS_32.png";
        } else if (filename.endsWith("ppt") || filename.endsWith("pptx")) {
            imageloc = "resources/PPT_32.png";
        } else if (filename.endsWith("ico") || filename.endsWith("png") || filename.endsWith("tiff")) {
            imageloc = "resources/Image_File_32.png";
        } else if (filename.endsWith("lock")) {
            imageloc = "resources/Lock_32.png";
        } else if (filename.endsWith("zip") || filename.endsWith("rar")) {
            imageloc = "resources/WinRAR_32.png";
        } else if (filename.endsWith("aes")) {
            imageloc = "resources/Lock_32.png";
        } else if (filename.endsWith("txt")) {
            imageloc = "resources/TXT_64.png";
        } else {
            imageloc = "resources/File_32.png";
        }
        ImageView xy = new ImageView(imageloc);
        xy.setFitHeight(90);
        xy.setFitWidth(80);
        String name;
        try {
            name = fileList.get(i).getFile_name().substring(0, fileList.get(i).getFile_name().lastIndexOf("."));
        } catch (Exception e) {
            name = fileList.get(i).getFile_id();
        }
        VBox xyz = new VBox(xy, new Label(name));
        xyz.setPadding(new Insets(10));
        xyz.setSpacing(10);
        xyz.setAlignment(Pos.CENTER);
        JFXToggleNode xyz1 = new JFXToggleNode();
        xyz1.setGraphic(xyz);
        xyz1.setMaxSize(100, 120);
        xyz1.setMinSize(100, 120);
        if (fileList.get(i).getDirectory().equals("shared")) {
            sharedwithme.getChildren().add(xyz1);
            xyz1.setToggleGroup(sfbuttonGroup);
            xyz1.setOnMouseClicked((event) -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    xyz1.setSelected(true);
                    sfpopup.show(xyz1, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, event.getX(), event.getY());
                } else {

                }
            });
        } else if (fileList.get(i).getDirectory().equals("mf") && fileList.get(i).getPredelete().equals("true")) {
            binpane.getChildren().add(xyz1);
            xyz1.setToggleGroup(binbuttonGroup);
            xyz1.setOnMouseClicked((event) -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    xyz1.setSelected(true);
                    binpopup.show(xyz1, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, event.getX(), event.getY());
                } else {

                }
            });
        } else if (fileList.get(i).getDirectory().equals("mf") && fileList.get(i).getPredelete().equals("false")) {
            myfiles.getChildren().add(xyz1);
            xyz1.setToggleGroup(mfbuttonGroup);
            xyz1.setOnMouseClicked((event) -> {
                if (event.getButton() == MouseButton.SECONDARY) {
                    xyz1.setSelected(true);
                    if (fileList.get(i).getFile_name().endsWith(".aes")) {
                        mfepopup.show(xyz1, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, event.getX(), event.getY());
                    } else {
                        mfpopup.show(xyz1, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT, event.getX(), event.getY());
                    }
                }

            });
        }
        xyz1.setUserData(i);
        xyz1.setTooltip(new Tooltip(name));
    }
//done

    @FXML
    private void uploadButtonAction(ActionEvent event) {
        filelocation.setText("");
        filelocation.setEditable(false);
        List<File> list1 = new ArrayList<File>();
        encryptpass.resetValidation();
        filelocation.resetValidation();
        encryptpass.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                encryptpass.validate();
            }
        });
        chooseFile.setOnAction((e) -> {
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Select upload files");
            filechooser.setInitialDirectory(new File(System.getProperty("user.home")));
            try {
                List<File> list = filechooser.showOpenMultipleDialog(uploadButton.getScene().getWindow());
                if (!list.isEmpty()) {
                    String loc = "";
                    for (int i = 0; i < list.size(); i++) {
                        loc = loc + "\n" + list.get(i).getName();
                    }
                    filelocation.setText(filelocation.getText().trim() + loc);
                    list1.addAll(list);
                }
            } catch (Exception ex) {
                filelocation.validate();
            }
        });

        checkEncrypt.setOnAction((e) -> {
            if (checkEncrypt.isSelected()) {
                encryptpass.setDisable(false);
            } else {
                encryptpass.setDisable(true);
            }
        });
        uploadconfirm.setOnAction((event1) -> {
            if (!list1.isEmpty() && (!checkEncrypt.isSelected() || (checkEncrypt.isSelected() && encryptpass.getText().length() == 16))) {
                upload(list1);
                uploaddialog.close();
            } else {
                filelocation.validate();
                if (checkEncrypt.isSelected() && encryptpass.getText().trim().length() != 16) {
                    encryptpass.validate();
                }
            }
        });
        uploaddialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        uploaddialog.show(rootpane);
    }
//done

    private void upload(List<File> list) {
        ExecutorService uploadService = Executors.newCachedThreadPool();
        List<Task<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Task task = uploadTask(list.get(i));
            uploadService.submit(task);
            tasks.add(task);
        }
        uploadtask.getTasks().addAll(tasks);
        uploadService.shutdown();
        new Thread(() -> {
            try {
                uploadService.awaitTermination(1, TimeUnit.MINUTES);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
//done

    public Task<Void> uploadTask(File file) {
        return new Task<Void>() {
            @Override
            protected Void call()
                    throws InterruptedException, UnsupportedEncodingException, IOException {
                updateMessage("Finding files . . .");
                updateProgress(0, 100);
                FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
                MultipartEntityBuilder builder = MultipartEntityBuilder.create();
                builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                builder.addPart("file", fileBody);
                if (checkEncrypt.isSelected()) {
                    builder.addPart("checkEncrypt", new StringBody("true", ContentType.DEFAULT_TEXT));
                    builder.addPart("pass", new StringBody(encryptpass.getText(), ContentType.DEFAULT_TEXT));
                } else {
                    builder.addPart("checkEncrypt", new StringBody("false", ContentType.DEFAULT_TEXT));
                }
                HttpEntity entity = builder.build();
                updateMessage(file.getName());
                ProgressListener listener = (float percentage) -> {
                    updateProgress(percentage, 100);
                };
                fileupload.setEntity(new ProgressEntityWrapper(entity, listener));
                try {
                    CloseableHttpResponse response = client.execute(fileupload, connect.getHttpContext());
                    HttpEntity responseentity = response.getEntity();
                    if (responseentity != null) {
                        switch (response.getStatusLine().getStatusCode()) {
                            case HttpStatus.SC_ACCEPTED:
                                Platform.runLater(() -> {
                                    Notifications.create()
                                            .title("Information")
                                            .text(file.getName() + " Uploaded").hideAfter(Duration.seconds(2))
                                            .showInformation();
                                    paintcontains();
                                });
                                break;
                            case HttpStatus.SC_CONFLICT:
                                Platform.runLater(() -> {
                                    Notifications.create()
                                            .title("Information")
                                            .text("File already exists").hideAfter(Duration.seconds(2))
                                            .showInformation();
                                });
                                break;
                            case HttpStatus.SC_NO_CONTENT:
                                Platform.runLater(() -> {
                                    Notifications.create()
                                            .title("Information")
                                            .text("File can't be encrypted").hideAfter(Duration.seconds(2))
                                            .showInformation();
                                });
                                break;
                            case HttpStatus.SC_UNAUTHORIZED:
                                Platform.runLater(() -> {
                                    Notifications.create()
                                            .title("Information")
                                            .text("Unauthorized access").hideAfter(Duration.seconds(2))
                                            .showInformation();
                                });
                                break;
                            default:
                                Platform.runLater(() -> {
                                    Notifications.create()
                                            .title("Information")
                                            .text("File not uploaded " + file.getName()).hideAfter(Duration.seconds(2))
                                            .showInformation();
                                });
                                break;
                        }
                    } else {
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("Check Connection").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                    }
                } catch (IOException ex) {
                    System.out.println(ex);
                }
                return null;
            }
        };
    }
//done

    private void download(int fileno) {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Select path");
        chooser.setInitialDirectory(new File(System.getProperty("user.home")));
        if (!fileList.get(fileno).getFile_name().endsWith(".aes")) {
            decryptpass.setDisable(true);
        } else {
            decryptpass.setDisable(false);
        }
        decryptPass.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                decryptPass.validate();
            }
        });
        downloc.resetValidation();
        decryptPass.resetValidation();
        downloc.setEditable(threadstatus);
        downloaddialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        downloaddialog.show(rootpane);
        downloadcancel.setOnAction(e -> downloaddialog.close());
        downlocbrowse.setOnAction(e -> {
            directory = chooser.showDialog(rootpane.getScene().getWindow());
            downloc.setText(directory.getAbsolutePath());
        });
        downloadConfirmbutton.setOnAction((e) -> {
            if (directory != null) {
                if ((!fileList.get(fileno).getFile_name().endsWith(".aes")) || ((fileList.get(fileno).getFile_name().endsWith(".aes")) && decryptpass.getText().length() == 16)) {
                    ExecutorService downloadService = Executors.newCachedThreadPool();
                    Task newdownloadtask = downloadTask(fileno, directory.getPath());
                    downloadService.submit(newdownloadtask);
                    downloadtask.getTasks().add(newdownloadtask);
                    downloadService.shutdown();
                    new Thread(() -> {
                        try {
                            downloadService.awaitTermination(1, TimeUnit.MINUTES);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MainsceneController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }).start();
                    downloaddialog.close();
                } else {
                    decryptpass.validate();
                }
            } else {
                downloc.validate();
            }
        });

    }
//done

    public Task<Void> downloadTask(int i, String filepath) {
        return new Task<Void>() {
            @Override
            protected Void call()
                    throws InterruptedException, UnsupportedEncodingException, IOException {
                updateMessage("Finding file . . .");
                updateProgress(0, 100);
                List<NameValuePair> params = new ArrayList<NameValuePair>(1);
                params.add(new BasicNameValuePair("fileid", fileList.get(i).getFile_id()));
                if (fileList.get(i).getFile_name().endsWith(".aes")) {
                    params.add(new BasicNameValuePair("pass", decryptpass.getText()));
                }
                try {
                    filedownload.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
                    HttpResponse response = httpclient.execute(filedownload, connect.getHttpContext());
                    HttpEntity entity = response.getEntity();
                    if (entity != null) {
                        switch (response.getStatusLine().getStatusCode()) {
                            case HttpStatus.SC_ACCEPTED:
                                BufferedInputStream bis = new BufferedInputStream(entity.getContent());
                                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(filepath + "//" + fileList.get(i).getFile_name().replace(".aes", ""))), 1024);
                                long filesize = entity.getContentLength();
                                long downloadedfilesize = 0;
                                int x = 0;
                                byte[] data = new byte[1024];
                                while ((x = bis.read(data, 0, 1024)) >= 0) {
                                    downloadedfilesize += x;
                                    updateProgress(downloadedfilesize, filesize);
                                    bos.write(data, 0, x);
                                }
                                bis.close();
                                bos.close();
                                Platform.runLater(() -> {
                                    Notifications.create()
                                            .title("Information")
                                            .text(fileList.get(i).getFile_name().replace(".aes", "") + " Downloaded").hideAfter(Duration.seconds(2))
                                            .showInformation();
                                });
                                break;
                            case HttpStatus.SC_CONFLICT:
                                Platform.runLater(() -> {
                                    Notifications.create()
                                            .title("Information")
                                            .text("Decryption error").hideAfter(Duration.seconds(2))
                                            .showInformation();
                                });
                                break;
                            default:
                                Platform.runLater(() -> {
                                    Notifications.create()
                                            .title("Information")
                                            .text("File not Downloaded ").hideAfter(Duration.seconds(2))
                                            .showInformation();
                                });
                                break;
                        }
                    } else {
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("Check Connection").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                    }
                    return null;
                } catch (UnsupportedEncodingException ex) {
                    return null;
                }

            }
        };
    }
//done

    private void share(ActionEvent event) {
        receiverid.resetValidation();
        sharerPassowrd.resetValidation();
        receiverid.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                receiverid.validate();
            }
        });
        sharerPassowrd.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                sharerPassowrd.validate();
            }
        });
        shareconfirm.setOnAction(e -> {
            if (!(receiverid.getText().trim().equals("") || sharerPassowrd.getText().trim().equals("") || receiverid.getText().equals(null) || sharerPassowrd.getText().equals(null))) {
                int status = connect.shareFile(receiverid.getText(), sharerPassowrd.getText(), fileList.get((int) mfbuttonGroup.getSelectedToggle().getUserData()).getFile_id());
                switch (status) {
                    case HttpStatus.SC_ACCEPTED:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File shared").hideAfter(Duration.seconds(2))
                                    .showInformation();
                            paintcontains();
                        });
                        break;
                    case HttpStatus.SC_FORBIDDEN:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("Entered password is wrong").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                    case HttpStatus.SC_CONFLICT:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("Person you want to share doesnt exist!").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                    default:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("Check Connection").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                }
                sharedialog.close();
            } else {
                if (receiverid.getText().trim().equals("") || receiverid.getText().equals(null)) {
                    receiverid.validate();
                }
                if (sharerPassowrd.getText().trim().equals("") || sharerPassowrd.getText().equals(null)) {
                    sharerPassowrd.validate();
                }
            }
        });
        sharedialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        sharedialog.maxHeight(100);
        sharedialog.maxWidth(200);
        sharedialog.show(rootpane);

    }
//done

    private void rename(ActionEvent e) {
        renamedialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        renamedialog.show(rootpane);
        int i = (int) mfbuttonGroup.getSelectedToggle().getUserData();
        String filename = fileList.get(i).getFile_name();
        renamefilename.setText(filename);
        fileextension.setText(filename.substring(filename.lastIndexOf(".")));
        newfileName.resetValidation();
        newfileName.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                newfileName.validate();
            }
        });
        renameConfirmbutton.setOnAction(event -> {
            String newname = newfileName.getText().trim();
            if (newname.equals("") || newname.equals(null)) {
                newfileName.validate();
            } else {
                int status = connect.fileRename(newname, fileList.get(i).getFile_id());
                switch (status) {
                    case HttpStatus.SC_ACCEPTED:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File renamed.").hideAfter(Duration.seconds(2))
                                    .showInformation();
                            paintcontains();
                        });
                        break;
                    case HttpStatus.SC_CONFLICT:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File with this name already exist.").hideAfter(Duration.seconds(2))
                                    .showInformation();
                            paintcontains();
                        });
                        break;
                    default:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("Check Connection").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                }
                renamedialog.close();
            }
        });
    }
//done    

    private void encrypt(ActionEvent e) {
        encryptdialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        encryptdialog.show(rootpane);
        int i = (int) mfbuttonGroup.getSelectedToggle().getUserData();
        encryptfilename.setText(fileList.get(i).getFile_name());
        newEncryptPass.resetValidation();
        userpass2.resetValidation();
        newEncryptPass.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                newEncryptPass.validate();
            }
        });
        userpass2.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                userpass2.validate();
            }
        });
        encryptConfirmbutton.setOnAction(event -> {
            String encryptpass = newEncryptPass.getText().trim();
            String pass = userpass2.getText().trim();
            if (!encryptpass.equals("") && !pass.equals("") && encryptpass.length() == 16) {
                switch (connect.fileEncrypt(fileList.get(i).getFile_id(), encryptpass, pass)) {
                    case HttpStatus.SC_ACCEPTED:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File Encrypted successfully.").hideAfter(Duration.seconds(2))
                                    .showInformation();
                            paintcontains();
                        });
                        break;
                    case HttpStatus.SC_CONFLICT:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File is already encrypted").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                    case HttpStatus.SC_FORBIDDEN:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("Check Connection").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                    default:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File Encryption unsucessful.").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                }
                encryptdialog.close();
            } else {
                if (pass.equals("") || pass.equals(null)) {
                    userpass2.validate();
                }
                if (encryptpass.length() != 16) {
                    newEncryptPass.validate();
                }
            }
        });
    }
//done

    private void decrypt(ActionEvent e) {
        decryptdialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        decryptdialog.show(rootpane);
        int i = (int) mfbuttonGroup.getSelectedToggle().getUserData();
        decryptfilename.setText(fileList.get(i).getFile_name());
        decryptPass.resetValidation();
        userpass3.resetValidation();
        decryptPass.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                decryptPass.validate();
            }
        });
        userpass3.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                userpass3.validate();
            }
        });
        decryptConfirmbutton.setOnAction(event -> {
            String decryptpass1 = decryptPass.getText().trim();
            String pass = userpass3.getText().trim();
            if (!decryptpass1.equals("") && !pass.equals("") && decryptpass1.length() == 16) {
                int status = connect.fileDecrypt(fileList.get(i).getFile_id(), decryptpass1, pass);
                switch (status) {
                    case HttpStatus.SC_ACCEPTED:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File encryption removed.").hideAfter(Duration.seconds(2))
                                    .showInformation();
                            paintcontains();
                        });
                        break;
                    case HttpStatus.SC_CONFLICT:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File is already decrypted.").hideAfter(Duration.seconds(2))
                                    .showInformation();
                            paintcontains();
                        });
                        break;
                    case 808:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("Check Connection").hideAfter(Duration.seconds(2))
                                    .showInformation();
                            paintcontains();
                        });
                        break;
                    default:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File decryption failed.").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                }
                decryptdialog.close();
            } else {
                if (decryptpass1.length() != 16) {
                    decryptPass.validate();
                }
                if (pass.equals("")) {
                    userpass3.validate();
                }
            }
        });
    }
//done

    private void delete(ActionEvent e) {
        int status = connect.filePredelete(fileList.get((int) mfbuttonGroup.getSelectedToggle().getUserData()).getFile_id());
        switch (status) {
            case HttpStatus.SC_ACCEPTED:
                Platform.runLater(() -> {
                    Notifications.create()
                            .title("Information")
                            .text("File moved to recycle bin.").hideAfter(Duration.seconds(2))
                            .showInformation();
                    paintcontains();
                });
                break;
            default:
                Platform.runLater(() -> {
                    Notifications.create()
                            .title("Information")
                            .text("check Connection.").hideAfter(Duration.seconds(2))
                            .showInformation();
                });
                break;
        }
    }
//done

    private void restore(ActionEvent e) {
        int status = connect.restoreFile(fileList.get((int) binbuttonGroup.getSelectedToggle().getUserData()).getFile_id());
        switch (status) {
            case HttpStatus.SC_ACCEPTED:
                Platform.runLater(() -> {
                    Notifications.create()
                            .title("Information")
                            .text("File restored.").hideAfter(Duration.seconds(2))
                            .showInformation();
                    paintcontains();
                });
                break;
            case HttpStatus.SC_FORBIDDEN:
                Platform.runLater(() -> {
                    Notifications.create()
                            .title("Information")
                            .text("File not restored.").hideAfter(Duration.seconds(2))
                            .showInformation();
                    paintcontains();
                });
                break;
            default:
                Platform.runLater(() -> {
                    Notifications.create()
                            .title("Information")
                            .text("Check Connection").hideAfter(Duration.seconds(2))
                            .showInformation();
                });
                break;
        }

    }
//done

    private void permadelete(ActionEvent event) {
        deletedialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        deletedialog.maxHeight(100);
        deletedialog.maxWidth(200);
        deletedialog.show(rootpane);
        userpass.resetValidation();
        int userdata = (int) binbuttonGroup.getSelectedToggle().getUserData();
        delefilename.setText("Dow you want to delete " + fileList.get(userdata).getFile_name() + "?");
        userpass.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                userpass.validate();
            }
        });
        deleteConfirmbutton.setOnAction(e -> {
            if (userpass.getText().trim().equals("") || userpass.getText().equals(null)) {
                userpass.validate();
            } else {
                switch (connect.filePermadelete(fileList.get(userdata).getFile_id(), userpass.getText().trim())) {
                    case HttpStatus.SC_ACCEPTED:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File deleted").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        paintcontains();
                        break;
                    case 808:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("Check Connection").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                    default:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File not deleted").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                }
                deletedialog.close();
            }
        });
    }
//dones

    @FXML
    private void refresh(ActionEvent event) {
        paintcontains();
    }

    @FXML
    private void sliderButtonPressed(MouseEvent event) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.4), slider_button);
        if (optionpane1.isVisible()) {
            menudrawer.setDividerPositions(0);
            optionpane.setMaxWidth(0);
            transition.setByX(-200);
            optionpane1.setVisible(false);
        } else {
            menudrawer.setDividerPositions(175 / menudrawer.getWidth());
            optionpane.setMaxWidth(175);
            optionpane1.setVisible(true);
            transition.setByX(200);
        }
        burgerTask.setRate(burgerTask.getRate() * -1);
        transition.play();
        burgerTask.play();
        sharedWithMe.setOnAction(e -> {
            swmpane.setVisible(true);
            paneTransition(toppane, swmpane);
            toppane = swmpane;
        });
        myFiles.setOnAction(e -> {
            myFiles.setVisible(true);
            paneTransition(toppane, mfpane);
            toppane = mfpane;
        });
        binbutton.setOnAction(e -> {
            binbutton.setVisible(true);
            paneTransition(toppane, recycleview);
            toppane = recycleview;
        });
        shareHistoryButton.setOnAction(e -> {
            shareHistoryButton.setVisible(true);
            paneTransition(toppane, sharehistory);
            toppane = sharehistory;
        });

    }

    private void rightclickpopup() {
        JFXButton encButton = new JFXButton("Encrypt");
        JFXButton decButton = new JFXButton("Decrypt");
        JFXButton delButton1 = new JFXButton("Delete");
        JFXButton delButton2 = new JFXButton("Delete");
        JFXButton delButton3 = new JFXButton("Permadelete");
        JFXButton restoreButton = new JFXButton("Restore");
        JFXButton remButton = new JFXButton("Unshare");
        JFXButton renameButton1 = new JFXButton("Rename");
        JFXButton renameButton2 = new JFXButton("Rename");
        JFXButton shareButton1 = new JFXButton("Share");
        JFXButton shareButton2 = new JFXButton("Share");
        JFXButton downloadButton1 = new JFXButton("Download");
        JFXButton downloadButton2 = new JFXButton("Download");
        JFXButton downloadButton3 = new JFXButton("Download");
        encButton.setOnAction(e -> {
            mfpopup.hide();
            encrypt(e);
        });
        decButton.setOnAction(e -> {
            mfepopup.hide();
            decrypt(e);
        });
        delButton1.setOnAction(e -> {
            mfpopup.hide();
            delete(e);
        });
        delButton2.setOnAction(e -> {
            mfepopup.hide();
            delete(e);
        });
        delButton3.setOnAction(e -> {
            binpopup.hide();
            permadelete(e);
        });
        restoreButton.setOnAction(e -> {
            binpopup.hide();
            restore(e);
        });
        remButton.setOnAction(e -> {
            sfpopup.hide();
            file f = fileList.get((int) sfbuttonGroup.getSelectedToggle().getUserData());
            unshare(f.getFile_id(), f.getFile_name());
        });
        renameButton1.setOnAction(e -> {
            mfpopup.hide();
            rename(e);
        });
        renameButton2.setOnAction(e -> {
            mfepopup.hide();
            rename(e);
        });
        shareButton1.setOnAction(e -> {
            mfpopup.hide();
            share(e);
        });
        shareButton2.setOnAction(e -> {
            mfepopup.hide();
            share(e);
        });
        downloadButton1.setOnAction(e -> {
            mfpopup.hide();
            download((int) mfbuttonGroup.getSelectedToggle().getUserData());
        });
        downloadButton2.setOnAction(e -> {
            mfepopup.hide();
            download((int) mfbuttonGroup.getSelectedToggle().getUserData());
        });
        downloadButton3.setOnAction(e -> {
            sfpopup.hide();
            download((int) sfbuttonGroup.getSelectedToggle().getUserData());
        });
        VBox mfpopuplist = new VBox(encButton, downloadButton1, shareButton1, renameButton1, delButton1);
        VBox mfepopuplist = new VBox(decButton, downloadButton2, shareButton2, renameButton2, delButton2);
        VBox sfpopuplist = new VBox(downloadButton3, remButton);
        VBox binpopuplist = new VBox(restoreButton, delButton3);
        mfpopup = new JFXPopup(mfpopuplist);
        mfepopup = new JFXPopup(mfepopuplist);
        sfpopup = new JFXPopup(sfpopuplist);
        binpopup = new JFXPopup(binpopuplist);
        mfpopup.getStyleClass().add("popupmenu");
        mfepopup.getStyleClass().add("popupmenu");
        sfpopup.getStyleClass().add("popupmenu");
        binpopup.getStyleClass().add("popupmenu");
    }

    private void paintShareHistoryTabel() {
        fileNameColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<sharedToFileList, String> param) -> {
            if (fileNameColumn.validateValue(param)) {
                return param.getValue().getValue().getFile_name();
            } else {
                return fileNameColumn.getComputedValue(param);
            }
        });
        fileSizeColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<sharedToFileList, String> param) -> {
            if (fileSizeColumn.validateValue(param)) {
                return param.getValue().getValue().getFile_size();
            } else {
                return fileSizeColumn.getComputedValue(param);
            }
        });
        sharedToColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<sharedToFileList, String> param) -> {
            if (sharedToColumn.validateValue(param)) {
                return param.getValue().getValue().getSharedto();
            } else {
                return sharedToColumn.getComputedValue(param);
            }
        });
        sharedOnColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<sharedToFileList, String> param) -> {
            if (sharedOnColumn.validateValue(param)) {
                return param.getValue().getValue().getShared_on();
            } else {
                return sharedOnColumn.getComputedValue(param);
            }
        });
        unsharebutton.setDisable(false);
        sharehistoryTreeTable.setRoot(new RecursiveTreeItem<sharedToFileList>(sharedFileList, RecursiveTreeObject::getChildren));
        sharehistoryTreeTable.setShowRoot(false);
        sharehistoryTreeTable.setEditable(false);
        searchFile.textProperty().addListener((observable, oldValue, newValue) -> {
            sharehistoryTreeTable.unGroup(fileNameColumn);
            sharehistoryTreeTable.unGroup(sharedToColumn);
            sharehistoryTreeTable.setPredicate(t -> t.getValue().getFile_name().get().contains(newValue) || t.getValue().getSharedto().get().contains(newValue) || t.getValue().getShared_on().get().contains(newValue));
        });
        groupByFiles.setOnAction((event) -> {
            new Thread(() -> {
                sharehistoryTreeTable.unGroup(sharedToColumn);
                sharehistoryTreeTable.group(fileNameColumn);
            }).start();
        });
        groupByUsers.setOnAction(e -> {
            new Thread(() -> {
                sharehistoryTreeTable.unGroup(fileNameColumn);
                sharehistoryTreeTable.group(sharedToColumn);
            }).start();
        });
        resetgroup.setOnAction(e -> {
            sharehistoryTreeTable.unGroup(fileNameColumn);
            sharehistoryTreeTable.unGroup(sharedToColumn);
        });
    }

    private void unshare(String id, String file_name) {
        unsharedialog.setTransitionType(JFXDialog.DialogTransition.CENTER);
        unsharedialog.show(rootpane);
        unsharefilename.setText(file_name);
        userpass4.resetValidation();
        userpass4.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                userpass4.validate();
            }
        });
        unshareConfirmbutton.setOnAction(event -> {
            String pass = userpass4.getText().trim();
            if (pass.equals("") || pass.equals(null)) {
                userpass4.validate();
            } else {
                switch (connect.unshareFile(id, pass)) {
                    case HttpStatus.SC_ACCEPTED:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File Unshared.").hideAfter(Duration.seconds(2))
                                    .showInformation();
                            paintcontains();
                        });
                        break;
                    case HttpStatus.SC_BAD_REQUEST:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("File not found.").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                    default:
                        Platform.runLater(() -> {
                            Notifications.create()
                                    .title("Information")
                                    .text("Check Connection").hideAfter(Duration.seconds(2))
                                    .showInformation();
                        });
                        break;
                }
                unsharedialog.close();
            }
        });
    }

    private void logoutToLogin(ActionEvent event) {
        threadstatus = false;
        if (backThread.isAlive()) {
            backThread.interrupt();
        }
        try {
            Parent loginscene = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.close();
            Stage newStage = new Stage();
            JFXDecorator decorator = new JFXDecorator(newStage, loginscene);
            decorator.customMaximizeProperty().setValue(false);
            decorator.setCustomMaximize(true);
            Scene scene = new Scene(decorator);
            scene.getStylesheets().add(Client.class.getResource("/resources/css/jfoenix-fonts.css").toExternalForm());
            scene.getStylesheets().add(Client.class.getResource("/resources/css/jfoenix-design.css").toExternalForm());
            scene.getStylesheets().add(Client.class.getResource("/resources/css/jfoenix-main-demo.css").toExternalForm());
            scene.getStylesheets().add(Client.class.getResource("/resources/css/treetableview.css").toExternalForm());
            newStage.setScene(scene);
            newStage.show();
        } catch (IOException ex) {
            haltexecution();
        }
    }

    private static interface ProgressListener {

        void progress(float percentage);
    }

    private static class ProgressEntityWrapper extends HttpEntityWrapper {

        private ProgressListener listener;

        public ProgressEntityWrapper(HttpEntity entity, ProgressListener listener) {
            super(entity);
            this.listener = listener;
        }

        @Override
        public void writeTo(OutputStream outstream) throws IOException {
            super.writeTo(new CountingOutputStream(outstream, listener, getContentLength()));
        }
    }

    private static class CountingOutputStream extends FilterOutputStream {

        private ProgressListener listener;
        private long transferred;
        private long totalBytes;

        public CountingOutputStream(OutputStream out, ProgressListener listener, long totalBytes) {
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

    void paneTransition(SplitPane pane1, SplitPane pane2) {
        final double startWidth = pane1.getWidth();
        if (pane1 != pane2) {
            pane2.setVisible(true);
            final Animation hidepane1 = new Transition() {
                {
                    setCycleDuration(Duration.millis(500));
                }

                @Override
                protected void interpolate(double frac) {
                    final double curWidth = startWidth * (1.0 - frac);
                    pane1.setTranslateX(-startWidth + curWidth);
                }
            };
            hidepane1.onFinishedProperty().set((EventHandler<ActionEvent>) (ActionEvent actionEvent1) -> {
                pane1.setVisible(false);
                pane2.toFront();
            });
            final Animation showpane2 = new Transition() {
                {
                    pane2.setVisible(true);
                    setCycleDuration(Duration.millis(500));
                }

                @Override
                protected void interpolate(double frac) {
                    final double curWidth = startWidth * frac;
                    pane2.setTranslateX(-startWidth + curWidth);
                }
            };
            pane2.setVisible(true);
            showpane2.play();
            hidepane1.play();
        }
    }

    @FXML
    void unshareButtonAction(ActionEvent event) {
        sharedToFileList file = sharehistoryTreeTable.getSelectionModel().getSelectedItem().getValue();
        unshare(file.getFile_id(), file.getFile_name().get());
    }

    void passSessionId(String sessionID) {
        sessionid = sessionID;
    }

    public void haltexecution() {
        connect.logout();
        Platform.exit();
        threadstatus = false;
        if (backThread.isAlive()) {
            backThread.interrupt();
        }
    }
}
