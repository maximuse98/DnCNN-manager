package com.controllers;

import com.connector.MatlabConnector;
import com.models.Network;
import com.tasks.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

public class MainController implements Initializable {
    private MatlabConnector matlabConnector;
    private Network selectedNetwork;

    @FXML
    public Pane statusPane;
    public Label progressStatusLabel;

    public AnchorPane createPane;
    public TextField layersNumField;
    public TextField networkNameField;

    public Tab trainTab;
    public AnchorPane trainPane;
    public TextField imageTrainDirectory;
    public TextField numTrainFilesField;
    public TextField epochsField;
    public TextField noiseField;

    public Tab infoTab;
    public Label networkSizeLabel;
    public Label inputSizeLabel;
    public Label lossFunctionLabel;
    public Label isNetworkTrained;

    public Tab denoiseTab;
    public AnchorPane denoisePane;
    public TextField imageDenoiseDirectory;
    public TextField imageOriginalDirectory;
    public AnchorPane denoiseResultTab;
    public Label squaredErrorLabel;
    public Label absoluteErrorLabel;

    public ListView networksListView;
    public Button retrainButton;
    public Label networkLabel;

    private List<ImageView> errorViews = new LinkedList<>();
    private List<Network> networks;

    public MainController() throws InterruptedException, ExecutionException, IOException {
        matlabConnector = MatlabConnector.getInstance();
        networksListView = new ListView<String>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        networks = FXCollections.observableArrayList(matlabConnector.getNetworks());
        ObservableList<String> networkNames = FXCollections.observableArrayList();

        for (Network network : networks) {
            networkNames.add(network.getName());
        }
        networksListView.setItems(networkNames);
        networkLabel.getStyleClass().add("label-network");
    }

    public void onDesignerClick() throws ExecutionException, InterruptedException {
        Task task = new LoadDesignerTask();
        progressStatusLabel.setText("Loading designer");
        statusPane.setVisible(true);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                (EventHandler<WorkerStateEvent>) t -> {
                    task.getValue();
                    statusPane.setVisible(false);
                });
        // Start the Task.
        new Thread(task).start();
    }
    public void onNetworkListClick(){
        String selectedNetworkName = (String) networksListView.getSelectionModel().getSelectedItem();

        for (Network network: networks) {
            if (network.getName().equals(selectedNetworkName)){
                selectedNetwork = network;
            }
        }
        if(selectedNetwork == null) {
            trainTab.setDisable(true);
            infoTab.setDisable(true);
            denoiseTab.setDisable(true);
            return;
        }
        networkSizeLabel.setText(Double.toString(selectedNetwork.getNetworkSize()));
        inputSizeLabel.setText("["+Double.toString(selectedNetwork.getInputSize()[0])+" "+Double.toString(selectedNetwork.getInputSize()[1])+"]");
        lossFunctionLabel.setText(selectedNetwork.getLossFunction());
        isNetworkTrained.setText(selectedNetwork.getTrained().toString());
        infoTab.setDisable(false);
        trainTab.setDisable(false);
        denoiseResultTab.setVisible(false);
        if(!selectedNetwork.getTrained()){
            retrainButton.setDisable(true);
            denoiseTab.setDisable(true);
        }else{
            retrainButton.setDisable(false);
            denoiseTab.setDisable(false);
        }
    }
    public void onShowNetGraphClick(){
        Task task = new ShowNetGraphTask(selectedNetwork);
        progressStatusLabel.setText("Loading graph");
        statusPane.setVisible(true);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                (EventHandler<WorkerStateEvent>) t -> {
                    task.getValue();
                    statusPane.setVisible(false);
                });
        // Start the Task.
        new Thread(task).start();
    }
    public void onUpdateClick(){
        selectedNetwork = null;
        infoTab.setDisable(true);
        trainTab.setDisable(true);
        denoiseTab.setDisable(true);
        denoiseResultTab.setVisible(false);

        LoadNetworksTask loadNetworksTask = new LoadNetworksTask();
        progressStatusLabel.setText("Loading networks");
        statusPane.setVisible(true);
        loadNetworksTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                (EventHandler<WorkerStateEvent>) t -> {
                    loadNetworksTask.getValue();
                    progressStatusLabel.setText("Loading networks info");
                    networksListView.setItems(null);
                    networks = FXCollections.observableArrayList(matlabConnector.getNetworks());
                    ObservableList<String> networkNames = FXCollections.observableArrayList();

                    for (Network network:networks) {
                        networkNames.add(network.getName());
                    }
                    networksListView.setItems(networkNames);
                    statusPane.setVisible(false);
                });
        new Thread(loadNetworksTask).start();
    }

    public void openDirectoryChooser(){
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory = directoryChooser.showDialog(imageTrainDirectory.getScene().getWindow());

        if(selectedDirectory != null){
            imageTrainDirectory.setText(selectedDirectory.getAbsolutePath());
        }
    }
    public void openFileChooserForNoiseImage(){
        FileChooser directoryChooser = new FileChooser();
        File selectedFile = directoryChooser.showOpenDialog(imageTrainDirectory.getScene().getWindow());

        if(selectedFile != null){
            imageDenoiseDirectory.setText(selectedFile.getAbsolutePath());
        }
    }
    public void openFileChooserForOriginalImage(){
        FileChooser directoryChooser = new FileChooser();
        File selectedFile = directoryChooser.showOpenDialog(imageTrainDirectory.getScene().getWindow());

        if(selectedFile != null){
            imageOriginalDirectory.setText(selectedFile.getAbsolutePath());
        }
    }

    public void onCreateNetClick(){
        removeErrorViews(createPane);
        TextField[] fields = {networkNameField,layersNumField};
        if(checkFieldsIsEmpty(fields,createPane) || !checkFieldIsNumber(layersNumField,createPane)){
            return;
        }

        Task task = new CreateNetworkTask(networkNameField.getText(),Integer.parseInt(layersNumField.getText()));
        progressStatusLabel.setText("Creating network");
        statusPane.setVisible(true);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                (EventHandler<WorkerStateEvent>) t -> {
                    task.getValue();
                    statusPane.setVisible(false);
                    onUpdateClick();
                });
        // Start the Task.
        new Thread(task).start();
    }
    public void onTrainNetClick(){
        removeErrorViews(createPane);
        TextField[] fields = {imageTrainDirectory,numTrainFilesField, epochsField, noiseField};
        if(checkFieldsIsEmpty(fields,trainPane) || !checkFieldIsNumber(numTrainFilesField,trainPane) || !checkFieldIsNumber(epochsField,trainPane) || !checkFieldIsDouble(noiseField,trainPane)) {
            return;
        }

        Task task = new TrainNetworkTask(selectedNetwork.getName(), imageTrainDirectory.getText(), Integer.valueOf(numTrainFilesField.getText()), false, Integer.valueOf(epochsField.getText()), Double.valueOf(noiseField.getText()));
        progressStatusLabel.setText("Starting training");
        statusPane.setVisible(true);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                (EventHandler<WorkerStateEvent>) t -> {
                    task.getValue();
                    statusPane.setVisible(false);
                    onUpdateClick();
                });
        // Start the Task.
        new Thread(task).start();
    }
    public void onRetrainNetClick(){
        removeErrorViews(createPane);
        TextField[] fields = {imageTrainDirectory,numTrainFilesField, epochsField, noiseField};
        if(checkFieldsIsEmpty(fields,trainPane) || !checkFieldIsNumber(numTrainFilesField,trainPane) || !checkFieldIsNumber(epochsField,trainPane) || !checkFieldIsDouble(noiseField, trainPane)){
            return;
        }

        Task task = new TrainNetworkTask(selectedNetwork.getName(),imageTrainDirectory.getText(),Integer.valueOf(numTrainFilesField.getText()),true,Integer.valueOf(epochsField.getText()), Double.valueOf(noiseField.getText()));
        progressStatusLabel.setText("Starting retraining");
        statusPane.setVisible(true);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                (EventHandler<WorkerStateEvent>) t -> {
                    task.getValue();
                    statusPane.setVisible(false);
                    onUpdateClick();
                });
        // Start the Task.
        new Thread(task).start();
    }
    public void onDenoiseClick(){
        removeErrorViews(createPane);
        TextField[] fields = {imageDenoiseDirectory};
        if(checkFieldsIsEmpty(fields,denoisePane)){
            return;
        }

        Task task;
        if(!imageOriginalDirectory.getText().isEmpty()){
            task = new DenoiseTask(selectedNetwork,imageDenoiseDirectory.getText(),imageOriginalDirectory.getText());
        } else{
            task = new DenoiseTask(selectedNetwork,imageDenoiseDirectory.getText());
        }
        progressStatusLabel.setText("Denoising");
        statusPane.setVisible(true);
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                (EventHandler<WorkerStateEvent>) t -> {
                    task.getValue();
                    statusPane.setVisible(false);
                    if(selectedNetwork.getSquaredError()!=null){
                        squaredErrorLabel.setText(selectedNetwork.getSquaredError().toString());
                        absoluteErrorLabel.setText(selectedNetwork.getAbsoluteError().toString());
                        denoiseResultTab.setVisible(true);
                    } else{
                        denoiseResultTab.setVisible(false);
                    }
                    //onUpdateClick();
                });
        // Start the Task.
        new Thread(task).start();
    }

    private boolean checkFieldsIsEmpty(TextField[] fields, AnchorPane selectedPane){
        boolean isEmpty = false;
        for (TextField t: fields) {
            if (t.getText().isEmpty()){
                createErrorView(t,selectedPane);
                isEmpty = true;
            }
        }
        return isEmpty;
    }
    private boolean checkFieldIsNumber(TextField field, AnchorPane selectedPane){
        try{
            Integer.valueOf(field.getText());
            return true;
        }catch (NumberFormatException e){
            field.clear();
            createErrorView(field, selectedPane);
            return false;
        }
    }
    private boolean checkFieldIsDouble(TextField field, AnchorPane selectedPane){
        try{
            Double.valueOf(field.getText());
            return true;
        }catch (NumberFormatException e){
            field.clear();
            createErrorView(field, selectedPane);
            return false;
        }
    }

    private void createErrorView(TextField field, AnchorPane selectedPane){
        ImageView errorView = new ImageView(new Image(getClass().getResource("/pictures/error.png").toString(), 25, 25, true, true));
        errorView.setX(field.getLayoutX()+150);
        errorView.setY(field.getLayoutY());
        errorView.setPreserveRatio(true);
        errorView.setFitHeight(25);
        errorView.setFitWidth(25);

        selectedPane.getChildren().add(errorView);

        errorViews.add(errorView);
    }
    private void removeErrorViews(AnchorPane selectedPane){
        for (ImageView errorView: errorViews) {
            selectedPane.getChildren().remove(errorView);
        }
        errorViews.clear();
    }
}
