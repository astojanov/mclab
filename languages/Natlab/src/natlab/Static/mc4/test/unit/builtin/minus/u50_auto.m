function [result] = u50_auto(n)
  x0 = ([0.62290169489,0.741786989261;0.795193565566,0.942450283777]*n > .5);
  x1 = char(32+80*[0.236048089737,0.103166034231;0.396058242611,0.154972270802]*n);
  result = minus(x0,x1);
end
