macro elemwise {
    rule { ( $out:ident <= $M:ident $op $x:expr ) } => {
        for (var i = 0, N = $M.mj_length; i < N; ++i) {
            mj_set($out, [i], mj_get($M, [i]) $op $x);
        }
    }

    rule { ( $out:ident <= $fn:expr $M:ident ) } => {
        for (var i = 0, N = $M.mj_length; i < N; ++i) {
            mj_set($out, [i], $fn(mj_get($M, [i])));
        }
    }
}

macro pairwise {
    rule { ( $out:ident <= $M1:ident $op $M2:ident ) } => {
        var m1_length = $M1.mj_length;
        var m2_length = $M2.mj_length;
        if (m1_length !== m2_length) throw "array sizes differ";
        for (var i = 0, n = m1_length; i < n; ++i) {
            var x = mj_get($M1, [i]);
            var y = mj_get($M2, [i]);
            mj_set($out, [i], x $op y)
        }
    }

    rule { ( $out:ident <= $fn:expr $M1:ident $M2:ident ) } => {
        if ($M1.length !== $M2.length) throw "array sizes differ";
        for (var i = 0, n = $M1.length; i < n; ++i) {
            var x = mj_get($M1, [i]);
            var y = mj_get($M2, [i]);
            mj_set($out, [i], $fn(x, y));
        }
    }
}


function mc_plus_SS(x, y) {
    return x+y;
}


function mc_plus_SM(x, m) {
    var out = mj_clone(m);
    elemwise(out <= m + x);
    return out;
}


function mc_plus_MS(m, x) {
    var out = mj_clone(m);
    elemwise(out <= m + x);
    return out;
}


function mc_plus_MM(m1, m2) {
    var out = mj_clone(m1);
    pairwise(out <= m1 + m2);
    return out;
}


function mc_minus_SS(x, y) {
    return x-y;
}


function mc_minus_SM(x, m) {
    var out = mj_clone(m);
    elemwise(out <= m - x);
    return out;
}


function mc_minus_MS(m, x) {
    var out = mj_clone(m);
    elemwise(out <= m - x);
    return out;
}


function mc_minus_MM(m1, m2) {
    var out = mj_clone(m1);
    pairwise(out <= m1 - m2);
    return out;
}



function mc_rem_SS(x, y) {
    return x % y;
}


function mc_rem_SM(x, m) {
    var out = mj_clone(m);
    elemwise(out <= m % x);
    return out;
}


function mc_rem_MS(m, x) {
    var out = mj_clone(m);
    elemwise(out <= m % x);
    return out;
}


function mc_rem_MM(m1, m2) {
    var out = mj_clone(m1);
    pairwise(out <= m1 % m2);
    return out;
}



function mc_mtimes_SS(x, y) {
    return x*y;
}


function mc_mtimes_SM(x, m) {
    var out = mj_clone(m);
    elemwise(out <= m * x);
    return out;
}


function mc_mtimes_MS(m, x) {
    var out = mj_clone(m);
    elemwise(out <= m * x);
    return out;
}


function mc_mtimes_MM(m1, m2) {
    throw "mc_mtimes_MM: not implemented";
}


function mc_mrdivide_SS(x, y) {
    return x / y;
}


function mc_mrdivide_SM(x, m) {
    var out = mj_clone(m);
    elemwise(out <= m / x);
    return out;
}


function mc_mrdivide_MS(m, x) {
    var out = mj_clone(m);
    elemwise(out <= m / x);
    return out;
}


function mc_mrdivide_MM(m1, m2) {
    throw "mc_mrdivide_MM: not implemented";
}

function mc_lt_SS(x, y) {
    return x<y;
}


function mc_lt_SM(x, m) {
    var out = mj_clone(m);
    elemwise(out <= m < x);
    return out;
}


function mc_lt_MS(m, x) {
    var out = mj_clone(m);
    elemwise(out <= m < x);
    return out;
}


function mc_lt_MM(m1, m2) {
    var out = mj_clone(m1);
    pairwise(out <= m1 < m2);
    return out;
}


function mc_gt_SS(x, y) {
    return x > y;
}


function mc_gt_SM(x, m) {
    var out = mj_clone(m);
    elemwise(out <= m > x);
    return out;
}


function mc_gt_MS(m, x) {
    var out = mj_clone(m);
    elemwise(out <= m > x);
    return out;
}


function mc_gt_MM(m1, m2) {
    var out = mj_clone(m1);
    pairwise(out <= m1 > m2);
    return out;
}


function mc_eq_SS(x1, x2) {
    return x1 === x2;
}


function mc_eq_SM(x, m) {
    var out = mj_clone(m);
    elemwise(out <= m === x);
    return out;
}


function mc_eq_MS(m, x) {
    var out = mj_clone(m);
    elemwise(out <= m === x);
    return out;
}


function mc_eq_MM(m1, m2) {
    var out = mj_clone(m1);
    pairwise(out <= m1 === m2);
    return out;
}

function mc_ne_SS(x1, x2) {
    return x1 !== x2;
}


function mc_ne_SM(x, m) {
    var out = mj_clone(m);
    elemwise(out <= m !== x);
    return out;
}


function mc_ne_MS(m, x) {
    var out = mj_clone(m);
    elemwise(out <= m !== x);
    return out;
}


function mc_ne_MM(m1, m2) {
    var out = mj_clone(m1);
    pairwise(out <= m1 !== m2);
    return out;
}


function mc_length_S(x) {
    return 1;
}


function mc_length_M(m) {
    return m.length;
}


function mc_sin_S(x) {
    return Math.sin(x);
}


function mc_sin_M(m) {
    var out = mj_clone(m);
    elemwise(out <= Math.sin m);
    return out;
}


function mc_uminus_S(x) {
    return -x;
}


function mc_uminus_M(m) {
    var out = mj_clone(m);
    for (var i = 0; i < mj_length(m); ++i)
        mj_set(out, [i], -mj_get(out, [i]));
    return out;
}


function mc_array_get(m, indices) {
    return mj_get(m, indices);
}


// TODO: handle array growth
function mc_array_set(m, indices, value) {
    return mj_set(m, indices, value);
}


// TODO: handle concatenating matrices
function mc_horzcat() {
    return mj_create(arguments, [1, arguments.length]);
}

// TODO: handle concatenating matrices
function mc_vertcat() {
    return mj_create(arguments, [arguments.length, 1]);
}


function mc_compute_shape_length(arg) {
    var shape, length;

    if (arg.length === 0) {
        shape = [1, 1];
        length = 1;
    }
    else if (arg.length === 1) {
        shape = [arg[0], arg[0]];
        length = arg[0] * arg[0];
    }
    else {
        shape = arg;
        length = 1;
        for (var i = 0; i < shape.length; ++i)
            length *= arg[i];
    }

    return [shape, length];
}

function mc_randn() {
    var sh_len = mc_compute_shape_length(Array.prototype.slice.call(arguments, 0));
    var shape = sh_len[0];
    var length = sh_len[1];

    if (length === 1)
        return Math.random();

    var buf = new Float64Array(length);
    for (var i = 0; i < length; ++i) {
        buf[i] = Math.random();
    }
    return mj_create(buf, shape);
}


function mc_zeros() {
    var sh_len = mc_compute_shape_length(Array.prototype.slice.call(arguments, 0));
    var shape = sh_len[0];
    var length = sh_len[1];

    if (length === 1)
        return 0;

    var buf = new Float64Array(length);
    return mj_create(buf, shape);
}


function mc_ones() {
    var sh_len = mc_compute_shape_length(Array.prototype.slice.call(arguments, 0));
    var shape = sh_len[0];
    var length = sh_len[1];

    if (length === 1)
        return Math.random();

    var buf = new Float64Array(length);
    for (var i = 0; i < length; ++i) {
        buf[i] = 1;
    }
    return mj_create(buf, shape);
}


function mc_eye(rows, cols) {
    if (cols === undefined)
        cols = rows;
    var buf = new Float64Array(rows * cols);
    var mat = mj_create(buf, [rows, cols]);
    for (var i = 0; i < rows; ++i) {
        mj_set(mat, [i, i], 1);
    }
    return mat;
}
