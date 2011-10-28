function [result] = u18_auto(n)
  x0 = single([0.844421851525,0.75795440294;0.420571580831,0.258916750293]*n);
  x1 = int8([0.956034271889,0.947827487059;0.0565513677268,0.0848719951589]*n);
  result = complex(x0,x1);
end
