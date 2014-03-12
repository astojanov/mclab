function [sign] = signint(x)
  if x < 0
     sign = -1
  elseif x == 0
    sign = 0
  else
    sign = 1
  end
end
