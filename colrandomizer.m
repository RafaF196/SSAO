clear

I = imread('empty.png');
I2 = I;

for i = 1:2533
    for j = 1:1280
        I2(i,j,1) = rand()*255;
        I2(i,j,2) = rand()*255;
        I2(i,j,3) = rand()*255;
    end
end

imwrite(I2, 'randcolor.png');