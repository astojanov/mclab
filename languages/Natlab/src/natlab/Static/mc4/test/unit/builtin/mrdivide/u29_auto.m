function [result] = u29_auto(n)
  x0 = int8([0.956034271889,0.947827487059;0.0565513677268,0.0848719951589]*n);
  x1 = double([0.134364244112,0.847433736937;0.763774618977,0.255069025739]*n);
  result = mrdivide(x0,x1);
end
