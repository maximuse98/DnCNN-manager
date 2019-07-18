function [rmse, peaksnr] = denoiseImWithOriginal(netName,imagePath,originalImagePath)
%     cd(netPath);
    struct = load(netName);
    net = struct.net;
    
    im = imread(imagePath);
    origIm = imread(originalImagePath);
    image = denoiseImage(im,net);
    
    subplot(1,2,1);
    imshow(im);
    subplot(1,2,2);
    imshow(image);
    
    mse = immse(origIm, image);
    rmse = sqrt(mse);
    rmse = round(rmse*10^4)/10^4;
    
    peaksnr = psnr(origIm,image);
    peaksnr = round(peaksnr*10^4)/10^4;
end