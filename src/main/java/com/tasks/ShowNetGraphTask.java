package com.tasks;

import com.connector.MatlabConnector;
import com.models.Network;
import javafx.concurrent.Task;

public class ShowNetGraphTask extends Task {
    private Network network;

    public ShowNetGraphTask(Network network) {
        this.network = network;
    }

    @Override
    protected Boolean call() throws Exception {
        try {
            MatlabConnector.getInstance().showNetGraph(network.getName()+".mat");
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
