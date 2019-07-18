package com.controllers;

import com.main.MainApp;
import com.tasks.ConnectTask;
import com.tasks.LoadNetworksTask;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class StartController {
    @FXML
    public Label spinnerTextLabel;
    public ProgressIndicator spinner;
    public Button startButton;

    public StartController() throws InterruptedException, ExecutionException, IOException {

    }

    @FXML
    private void onStartClick() throws IOException, ExecutionException, InterruptedException {
        startButton.setDisable(true);
        // Create a Task.
        Task connectTask = new ConnectTask();
        spinner.setVisible(true);
        spinnerTextLabel.setText("Starting Matlab");
        connectTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                (EventHandler<WorkerStateEvent>) t -> {
                    connectTask.getValue();
                    spinnerTextLabel.setText("Loading networks");
                    loadNetworks();
                });
        // Start the Task.
        new Thread(connectTask).start();
    }
    private void loadNetworks(){
        LoadNetworksTask loadNetworksTask = new LoadNetworksTask();
        loadNetworksTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                (EventHandler<WorkerStateEvent>) t -> {
                    loadNetworksTask.getValue();
                    spinnerTextLabel.setText("Done!");
                    try {
                        startMainApp();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        new Thread(loadNetworksTask).start();
    }
    private void startMainApp() throws IOException {
        String fxmlFile = "/fxml/main.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));
        Stage mainStage = new Stage();
        mainStage.getIcons().add(new Image(MainApp.class.getResourceAsStream("/pictures/neural-network-icon.png")));
        mainStage.setTitle("MainApp");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/css/main.css");
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();

        Stage stage  = (Stage) startButton.getScene().getWindow();
        stage.close();
    }
}

