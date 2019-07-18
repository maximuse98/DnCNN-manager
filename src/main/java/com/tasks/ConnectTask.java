package com.tasks;

import com.connector.MatlabConnector;
import javafx.concurrent.Task;

public class ConnectTask extends Task {
    @Override
    protected MatlabConnector call() throws Exception {
        return MatlabConnector.getInstance();
    }
}
