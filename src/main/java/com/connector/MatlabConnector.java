package com.connector;

import com.mathworks.engine.MatlabEngine;
import com.models.Network;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MatlabConnector {

    private MatlabEngine matlabEngine;
    private static MatlabConnector matlabConnector = null;
    private String networksPath;

    private List<Network> networks;

    private MatlabConnector() throws ExecutionException, InterruptedException, IOException {
        System.loadLibrary("nativemvm");
        Future<MatlabEngine> eng = MatlabEngine.startMatlabAsync();
        this.matlabEngine = eng.get();

        FileInputStream fis = new FileInputStream("src/main/resources/properties/connect.prop");
        Properties p = new Properties ();
        p.load(fis);
        String scriptsPath = System.getProperty("user.dir") + p.get("scriptsPath");
        networksPath = System.getProperty("user.dir") + p.get("networksPath");

        matlabEngine.eval("addpath( '" + scriptsPath + "');");
        matlabEngine.eval("cd '" + networksPath + "';");
    }

    public static MatlabConnector getInstance() throws ExecutionException, InterruptedException, IOException {
        if(matlabConnector == null){
            matlabConnector = new MatlabConnector();
        }
        return matlabConnector;
    }

    public void createNetwork(String networkName, int layersNum) throws ExecutionException, InterruptedException {
        matlabEngine.eval("createNet('"+networkName+"',"+layersNum+");");
    }
    public void trainNetwork(String networkName, String imagesPath, int numTrainFiles, int epochsNum, double noiseLevel) throws ExecutionException, InterruptedException {
        matlabEngine.eval("trainNetWithLayers('"+networkName+"','"+imagesPath+"',"+numTrainFiles+","+epochsNum+","+noiseLevel+");");
    }
    public void retrainNetwork(String networkName, String imagesPath, int numTrainFiles, int epochsNum, double noiseLevel) throws ExecutionException, InterruptedException {
        matlabEngine.eval("trainNetWithInitLayers('"+networkName+"','"+imagesPath+"',"+numTrainFiles+","+epochsNum+","+noiseLevel+");");
    }
    public void showNetGraph(String networkName) throws ExecutionException, InterruptedException {
        matlabEngine.eval("showNetGraph('"+networkName+"');");
    }
    public void openNetworkDesigner() throws ExecutionException, InterruptedException {
        matlabEngine.eval("deepNetworkDesigner");
    }
    public void denoiseImage(Network network, String imagePath, String originalImagePath) throws ExecutionException, InterruptedException {
        String networkName = network.getName();
        if(originalImagePath==null) {
            matlabEngine.eval("denoiseIm('" + networkName + "','" + imagePath + "');");
        }else{
            matlabEngine.eval("[ans1 ans2] = denoiseImWithOriginal('" + networkName + "','" + imagePath + "','"+originalImagePath+"');");
            Double squaredError = matlabEngine.getVariable("ans1");
            Double absoluteError = matlabEngine.getVariable("ans2");
            network.setSquaredError(squaredError);
            network.setAbsoluteError(absoluteError);
        }
    }

    public void loadNetworks() throws InterruptedException, ExecutionException, IOException {
        networks = new ArrayList<>();
        File folder = new File(networksPath);
        File[] files = folder.listFiles();
        for (File file: files) {
            if(file.getName().contains(".mat")){
                try {
                    Network network = new Network(file.getName(), file.getAbsolutePath());
                    networks.add(network);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    public void loadNetworkInfo(String networkName, Network network) throws ExecutionException, InterruptedException {
        matlabEngine.eval("[ans1 ans2 ans3 ans4 ans5] = loadNetInfo('"+networkName+"');");
        Boolean isTrained = matlabEngine.getVariable("ans1");
        network.setTrained(isTrained);
        Double[] inputSize = {matlabEngine.getVariable("ans3"), matlabEngine.getVariable("ans4")};

        network.setNetworkSize(matlabEngine.getVariable("ans2"));
        network.setInputSize(inputSize);
        network.setLossFunction(matlabEngine.getVariable("ans5"));
    }

    public List<Network> getNetworks() {
        return networks;
    }
}
