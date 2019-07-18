function trainNetWithLayers(netName,imagesPath,numTrainFiles,epochsNum,gaussianNoiseLevel)
%     cd(netPath);
    struct = load(netName);
    layers = struct.net.Layers;
    
    trainNet(netName,imagesPath,numTrainFiles,layers,epochsNum,gaussianNoiseLevel);
end