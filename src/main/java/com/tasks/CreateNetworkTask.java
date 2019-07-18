package com.tasks;

import com.connector.MatlabConnector;
import javafx.concurrent.Task;

public class CreateNetworkTask extends Task {
    private String networkName;
    private int layersNum;

    public CreateNetworkTask(String networkName, int layersNum) {
        this.networkName = networkName;
        this.layersNum = layersNum;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MatlabConnector.getInstance().createNetwork(networkName,layersNum);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
