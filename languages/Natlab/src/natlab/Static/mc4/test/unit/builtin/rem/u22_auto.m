function [result] = u22_auto(n)
  x0 = double([0.134364244112,0.847433736937;0.763774618977,0.255069025739]*n);
  x1 = single([0.844421851525,0.75795440294;0.420571580831,0.258916750293]*n);
  result = rem(x0,x1);
end
