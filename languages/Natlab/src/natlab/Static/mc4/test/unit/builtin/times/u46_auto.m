function [result] = u46_auto(n)
  x0 = ([0.62290169489,0.741786989261;0.795193565566,0.942450283777]*n > .5);
  x1 = single([0.844421851525,0.75795440294;0.420571580831,0.258916750293]*n);
  result = times(x0,x1);
end
