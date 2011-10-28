function [result] = u48_auto(n)
  x0 = ([0.62290169489,0.741786989261;0.795193565566,0.942450283777]*n > .5);
  x1 = int8([0.956034271889,0.947827487059;0.0565513677268,0.0848719951589]*n);
  result = mtimes(x0,x1);
end
