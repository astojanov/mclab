function [ys] = addone(xs)
  for i = 1:length(xs)
      ys(i) = xs(i) + 1;
  end
end
