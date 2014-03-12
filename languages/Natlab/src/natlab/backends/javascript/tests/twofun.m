function [y] = twofun(x)
  y = f(x) + f(x);
end

function [y] = f(x)
  y = 2*x + 1;
end
