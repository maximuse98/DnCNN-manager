function trainNet(netName,imagesPath,numTrainFiles,initLayers,epochsNum,gaussianNoiseLevel)
    imds = imageDatastore(imagesPath, ...
        'IncludeSubfolders',false,...
        'LabelSource','foldernames');
    [imdsTrain,imdsValidation] = splitEachLabel(imds,numTrainFiles,'randomize');
    
    trainFilesSize = [50 50];
    dnds = denoisingImageDatastore(imdsTrain,...
        'PatchesPerImage',60,...
        'PatchSize',trainFilesSize,...
        'ChannelFormat','grayscale',...);
        'GaussianNoiseLevel',gaussianNoiseLevel);
    %     'PatchSize',50,...
    dnds1 = denoisingImageDatastore(imdsValidation,...
        'PatchesPerImage',60,...
        'PatchSize',trainFilesSize,...
        'ChannelFormat','grayscale');
    
    options = trainingOptions('sgdm', ...
        'InitialLearnRate',1e-4, ...    % ��������� �������� ��������
        'MaxEpochs',epochsNum, ...      % ���������� ����
        'Shuffle','every-epoch', ...    % "�����������" ������
        'ValidationFrequency',2, ...    % ����� �������� ����� �������� ������ ��������
        'ValidationData',dnds1, ...     % ����������� ���������
        'Momentum',0.9,...              % ������� ���������� �������� (�� 0 �� 1)
        'Verbose',false, ...            % ����������� ���������� � ���� �������� (0 ��� 1)
        'Plots','training-progress',... % ����������� ������� � ���� ��������
        'ExecutionEnvironment','cpu'...     
    );
    net = trainNetwork(dnds,initLayers,options);
    save(netName,'net','initLayers');
end