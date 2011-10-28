function [result] = u39_auto(n)
  x0 = uint64([0.237964627092,0.544229225296;0.369955166548,0.603920038596]*n);
  x1 = ([0.62290169489,0.741786989261;0.795193565566,0.942450283777]*n > .5);
  result = times(x0,x1);
end
