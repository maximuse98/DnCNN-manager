package com.models;

import com.connector.MatlabConnector;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.concurrent.ExecutionException;

public class Network {

    private String name;
    private String path;

    private Boolean isTrained;
    private String lossFunction;
    private Double[] inputSize;
    private Double networkSize;

    private Double squaredError;
    private Double absoluteError;

    public Network(String name, String path) throws InterruptedException, ExecutionException, IOException {
        MatlabConnector.getInstance().loadNetworkInfo(name,this);

        this.name = name.substring(0, name.length() - 4);
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLossFunction() {
        return lossFunction;
    }

    public void setLossFunction(String lossFunction) {
        this.lossFunction = lossFunction;
    }

    public Double[] getInputSize() {
        return inputSize;
    }

    public void setInputSize(Double[] inputSize) {
        this.inputSize = inputSize;
    }

    public Double getNetworkSize() {
        return networkSize;
    }

    public void setNetworkSize(Double networkSize) {
        this.networkSize = networkSize;
    }

    public Boolean getTrained() {
        return isTrained;
    }

    public void setTrained(Boolean trained) {
        isTrained = trained;
    }

    public Double getSquaredError() {
        return squaredError;
    }

    public void setSquaredError(Double squaredError) {
        this.squaredError = squaredError;
    }

    public Double getAbsoluteError() {
        return absoluteError;
    }

    public void setAbsoluteError(Double absoluteError) {
        this.absoluteError = absoluteError;
    }
}
