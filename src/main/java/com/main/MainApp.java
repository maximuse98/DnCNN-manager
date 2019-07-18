package com.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.sun.org.apache.xalan.internal.utils.SecuritySupport.getResourceAsStream;


public class MainApp extends Application {
    public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
        launch(args);
    }


    public void start(final Stage stage) throws Exception {
        String fxmlFile = "/fxml/start.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));
        stage.setTitle("Launch");
        stage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/pictures/neural-network-icon.png")));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/css/start.css");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }
}
