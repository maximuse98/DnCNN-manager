package com.tasks;

import com.connector.MatlabConnector;
import javafx.concurrent.Task;

public class TrainNetworkTask extends Task {
    private String networkName;
    private String imagesPath;
    private int numTrainFiles;
    private boolean isTrain;
    private int epochsNum;
    private double noiseLevel;

    public TrainNetworkTask(String networkName, String imagesPath, int numTrainFiles, boolean isTrain, int epochsNum, double noiseLevel) {
        this.networkName = networkName;
        this.imagesPath = imagesPath;
        this.numTrainFiles = numTrainFiles;
        this.isTrain = isTrain;
        this.epochsNum = epochsNum;
        this.noiseLevel = noiseLevel;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            if(isTrain) {
                MatlabConnector.getInstance().trainNetwork(networkName, imagesPath, numTrainFiles, epochsNum, noiseLevel);
            }else {
                MatlabConnector.getInstance().retrainNetwork(networkName, imagesPath, numTrainFiles, epochsNum, noiseLevel);
            }
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
