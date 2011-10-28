function [result] = u31_auto(n)
  x0 = int8([0.956034271889,0.947827487059;0.0565513677268,0.0848719951589]*n);
  x1 = uint64([0.237964627092,0.544229225296;0.369955166548,0.603920038596]*n);
  result = dot(x0,x1);
end
