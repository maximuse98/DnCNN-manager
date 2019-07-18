package com.tasks;

import com.connector.MatlabConnector;
import javafx.concurrent.Task;

public class LoadDesignerTask extends Task {
    @Override
    protected Boolean call() throws Exception {
        try {
            MatlabConnector.getInstance().openNetworkDesigner();
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
