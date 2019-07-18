function denoiseIm(netName,imagePath)
%     cd(netPath);
    struct = load(netName);
    net = struct.net;
    
    im = imread(imagePath);
    image = denoiseImage(im,net);
    
    subplot(1,2,1);
    imshow(im);
    subplot(1,2,2);
    imshow(image);
    
end