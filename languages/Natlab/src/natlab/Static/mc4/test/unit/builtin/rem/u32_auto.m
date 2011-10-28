function [result] = u32_auto(n)
  x0 = int8([0.956034271889,0.947827487059;0.0565513677268,0.0848719951589]*n);
  x1 = char(32+80*[0.236048089737,0.103166034231;0.396058242611,0.154972270802]*n);
  result = rem(x0,x1);
end
