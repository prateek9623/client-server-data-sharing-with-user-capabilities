package client;

import com.jfoenix.controls.JFXDecorator;
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Client extends Application {
    private FXMLLoader fXMLLoader;
    @Override
    public void start(Stage stage) throws Exception {
        URL location = getClass().getResource("/resources/fxml/loginscene.fxml");
        fXMLLoader = new FXMLLoader(location);
        fXMLLoader.setBuilderFactory(new JavaFXBuilderFactory());
        Parent root = fXMLLoader.load(location.openStream());
        JFXDecorator decorator = new JFXDecorator(stage, root);
        decorator.customMaximizeProperty().setValue(false);
        decorator.setCustomMaximize(true);
	Scene scene = new Scene(decorator);
        scene.getStylesheets().add(Client.class.getResource("/resources/css/jfoenix-fonts.css").toExternalForm());
	scene.getStylesheets().add(Client.class.getResource("/resources/css/jfoenix-design.css").toExternalForm());
	scene.getStylesheets().add(Client.class.getResource("/resources/css/jfoenix-main-demo.css").toExternalForm());
        stage.setScene(scene);
        stage.show();
    }
    @Override
    public void stop() throws IOException{
        LoginsceneController name = (LoginsceneController)fXMLLoader.getController();
        name.haltexecution();
    }
    public static void main(String[] args) {
        launch(args);
    }
    
}
