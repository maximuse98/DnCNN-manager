function [isTrained,layersNum,length,width,netFunct] = loadNetInfo(netName)
    struct = load(netName);
    try
        net = struct.net;
        isTrained = true;
    catch
        isTrained = false;
    end
    
    layers = struct.initLayers;
%     netSize = 0;
    layersNum = size(layers,1);
    if (layersNum == 1)
        layersNum = size(layers,2);
    end
%     for i=1:layersNum
%         if contains(layers(i).Name,'Conv')
%             netSize = netSize+1;
%         end
%     end
    length = layers(1).InputSize(1);
    width = layers(1).InputSize(2);
    netFunct = layers(layersNum).LossFunction;
end