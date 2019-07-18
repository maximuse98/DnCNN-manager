package com.tasks;

import com.connector.MatlabConnector;
import javafx.concurrent.Task;

public class LoadNetworksTask extends Task {
    @Override
    protected Boolean call() throws Exception {
        try{
            MatlabConnector.getInstance().loadNetworks();
            //Thread.sleep(5000);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
