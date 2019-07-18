function trainNetWithInitLayers(netName,imagesPath,numTrainFiles,epochsNum,gaussianNoiseLevel)
%     cd(netPath);
    struct = load(netName);
    layers = struct.initLayers;
    
    trainNet(netName,imagesPath,numTrainFiles,layers,epochsNum,gaussianNoiseLevel);
end