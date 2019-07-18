package com.tasks;

import com.connector.MatlabConnector;
import com.models.Network;
import javafx.concurrent.Task;

public class DenoiseTask extends Task{
    private Network network;
    private String imagePath;
    private String originalImagePath;

    public DenoiseTask(Network network, String imagePath) {
        this.network = network;
        this.imagePath = imagePath;
    }

    public DenoiseTask(Network network, String imagePath, String originalImagePath) {
        this.network = network;
        this.imagePath = imagePath;
        this.originalImagePath = originalImagePath;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MatlabConnector.getInstance().denoiseImage(network,imagePath,originalImagePath);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
