function [cap] = drv_capr(scale)
%%
%% Driver to compute the capacitance of a transmission line using
%% finite difference and Gauss-Seidel iteration.
%%



a = (0.3257463)*2; % the numbers in parentheses are "rand's" made deterministic
b = 8.65*(0.04039);
c = 3.29*(0.55982);
d = (0.727561)*6.171;

n = floor(56.0980*(0.36));
tol = 1.3e-13; % Tolerance.
rel = 0.90; % Relaxation parameter.
t1 = clock;
for time = 1:scale*10
  cap = capacitor(a, b, c, d, n, tol, rel);
end

%t2 = clock;

% Compute the running time in seconds
%time = (t2-t1)*[0 0 86400 3600 60 1]';

disp(cap);
disp(time);

% Store the benchmark output
%output = {mean(cap(:))};

% No validation performed
%valid = 'N/A';

