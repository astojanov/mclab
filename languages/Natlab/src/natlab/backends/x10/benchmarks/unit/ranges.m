function [x] = ranges(A)
x= ones(3,4,5);
x(1,2,3) =9;
y=x(1,1:4,:);
%forTest1(x);
%disp(y);
end

