function createNet(netName,layersNum)
%     cd(netPath);
    initLayers = dnCNNLayers('NetworkDepth',layersNum);
    initLayers = initLayers';
%     trained = false(1);
    save(netName,'initLayers');
end