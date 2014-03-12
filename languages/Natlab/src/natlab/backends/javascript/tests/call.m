function [] = call(x)
  disp(x);
  y = sin(x);
  [a,b] = f(x);

  o = ones(10);
  z = f(o(1,1));
end

function [x1,x2] = f(x)
  x1 = -x;
  x2 =  x;
end
