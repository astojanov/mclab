function [result] = u74_auto(n)
  x0 = double([0.134364244112]*n);
  x1 = single([0.844421851525]*n);
  x2 = int8([0.956034271889]*n);
  result = colon(x0,x1,x2);
end
