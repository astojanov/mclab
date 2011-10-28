function [result] = u41_auto(n)
  x0 = char(32+80*[0.236048089737,0.103166034231;0.396058242611,0.154972270802]*n);
  x1 = double([0.134364244112,0.847433736937;0.763774618977,0.255069025739]*n);
  result = ldivide(x0,x1);
end
