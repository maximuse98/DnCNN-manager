function showNetGraph(netName)
%     cd(netPath);
    struct = load(netName);
    
    layers = struct.initLayers;
    
    graph = layerGraph(layers);
    plot(graph);    
end