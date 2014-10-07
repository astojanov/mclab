function mc_plus_SS(x, y) {
    return x + y;
}
function mc_plus_SM(x, m) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) + x);
    }
    ;
    return out;
}
function mc_plus_MS(m, x) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) + x);
    }
    ;
    return out;
}
function mc_plus_MM(m1, m2) {
    var out = mj_clone(m1);
    var m1_length = m1.mj_length;
    var m2_length = m2.mj_length;
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = mj_get(m1, [i]);
        var y = mj_get(m2, [i]);
        mj_set(out, [i], x + y);
    }
    ;
    return out;
}
function mc_minus_SS(x, y) {
    return x - y;
}
function mc_minus_SM(x, m) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) - x);
    }
    ;
    return out;
}
function mc_minus_MS(m, x) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) - x);
    }
    ;
    return out;
}
function mc_minus_MM(m1, m2) {
    var out = mj_clone(m1);
    var m1_length = m1.mj_length;
    var m2_length = m2.mj_length;
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = mj_get(m1, [i]);
        var y = mj_get(m2, [i]);
        mj_set(out, [i], x - y);
    }
    ;
    return out;
}
function mc_rem_SS(x, y) {
    return x % y;
}
function mc_rem_SM(x, m) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) % x);
    }
    ;
    return out;
}
function mc_rem_MS(m, x) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) % x);
    }
    ;
    return out;
}
function mc_rem_MM(m1, m2) {
    var out = mj_clone(m1);
    var m1_length = m1.mj_length;
    var m2_length = m2.mj_length;
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = mj_get(m1, [i]);
        var y = mj_get(m2, [i]);
        mj_set(out, [i], x % y);
    }
    ;
    return out;
}
function mc_mtimes_SS(x, y) {
    return x * y;
}
function mc_mtimes_SM(x, m) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) * x);
    }
    ;
    return out;
}
function mc_mtimes_MS(m, x) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) * x);
    }
    ;
    return out;
}
function mc_mtimes_MM(m1, m2) {
    throw 'mc_mtimes_MM: not implemented';
}
function mc_mrdivide_SS(x, y) {
    return x / y;
}
function mc_mrdivide_SM(x, m) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) / x);
    }
    ;
    return out;
}
function mc_mrdivide_MS(m, x) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) / x);
    }
    ;
    return out;
}
function mc_mrdivide_MM(m1, m2) {
    throw 'mc_mrdivide_MM: not implemented';
}
function mc_lt_SS(x, y) {
    return x < y;
}
function mc_lt_SM(x, m) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) < x);
    }
    ;
    return out;
}
function mc_lt_MS(m, x) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) < x);
    }
    ;
    return out;
}
function mc_lt_MM(m1, m2) {
    var out = mj_clone(m1);
    var m1_length = m1.mj_length;
    var m2_length = m2.mj_length;
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = mj_get(m1, [i]);
        var y = mj_get(m2, [i]);
        mj_set(out, [i], x < y);
    }
    ;
    return out;
}
function mc_gt_SS(x, y) {
    return x > y;
}
function mc_gt_SM(x, m) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) > x);
    }
    ;
    return out;
}
function mc_gt_MS(m, x) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) > x);
    }
    ;
    return out;
}
function mc_gt_MM(m1, m2) {
    var out = mj_clone(m1);
    var m1_length = m1.mj_length;
    var m2_length = m2.mj_length;
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = mj_get(m1, [i]);
        var y = mj_get(m2, [i]);
        mj_set(out, [i], x > y);
    }
    ;
    return out;
}
function mc_eq_SS(x1, x2) {
    return x1 === x2;
}
function mc_eq_SM(x, m) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) === x);
    }
    ;
    return out;
}
function mc_eq_MS(m, x) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) === x);
    }
    ;
    return out;
}
function mc_eq_MM(m1, m2) {
    var out = mj_clone(m1);
    var m1_length = m1.mj_length;
    var m2_length = m2.mj_length;
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = mj_get(m1, [i]);
        var y = mj_get(m2, [i]);
        mj_set(out, [i], x === y);
    }
    ;
    return out;
}
function mc_ne_SS(x1, x2) {
    return x1 !== x2;
}
function mc_ne_SM(x, m) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) !== x);
    }
    ;
    return out;
}
function mc_ne_MS(m, x) {
    var out = mj_clone(m);
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], mj_get(m, [i]) !== x);
    }
    ;
    return out;
}
function mc_ne_MM(m1, m2) {
    var out = mj_clone(m1);
    var m1_length = m1.mj_length;
    var m2_length = m2.mj_length;
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = mj_get(m1, [i]);
        var y = mj_get(m2, [i]);
        mj_set(out, [i], x !== y);
    }
    ;
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
    for (var i = 0, N = m.mj_length; i < N; ++i) {
        mj_set(out, [i], Math.sin(mj_get(m, [i])));
    }
    ;
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
// TODO: handle concatenating matrices of more than 2 dimensions
function mc_horzcat() {
    var num_rows = -1;
    var num_cols = 0;
    var len = 0;
    // Compute the length and number of columns of the result.
    // Also check that all arguments have the same number of rows.
    for (var i = 0; i < arguments.length; ++i) {
        if (num_rows == -1) {
            num_rows = mj_shape(arguments[i])[0];
        } else if (mj_shape(arguments[i])[0] != num_rows) {
            throw 'Dimensions of matrices being concatenated are not consistent.';
        }
        num_cols += mj_shape(arguments[i])[1];
        len += mj_length(arguments[i]);
    }
    // Create the result array buffer and populate it by just putting
    // all the arguments back-to-back.
    var buf = new Float64Array(len);
    var offset = 0;
    for (var i = 0; i < arguments.length; ++i) {
        if (mj_scalar(arguments[i])) {
            buf[offset] = arguments[i];
        } else {
            buf.set(arguments[i], offset);
        }
        offset += mj_length(arguments[i]);
    }
    return mj_create(buf, [
        num_rows,
        num_cols
    ]);
}
// TODO: handle concatenating matrices
function mc_vertcat() {
    var num_rows = 0;
    var num_cols = -1;
    var len = 0;
    for (var i = 0; i < arguments.length; ++i) {
        if (num_cols == -1) {
            num_cols = mj_shape(arguments[i])[1];
        } else if (mj_shape(arguments[i])[1] != num_cols) {
            throw 'Dimensions of matrices being concatenated are not consistent.';
        }
        num_rows += mj_shape(arguments[i])[0];
        len += mj_length(arguments[i]);
    }
    var buf = new Float64Array(len);
    var offset = 0;
    for (var col = 0; col < num_cols; ++col) {
        for (var arg_id = 0; arg_id < arguments.length; ++arg_id) {
            for (var row = 0; row < mj_shape(arguments[arg_id])[0]; ++row) {
                buf[offset] = mc_array_get(arguments[arg_id], [
                    row,
                    col
                ]);
                offset++;
            }
        }
    }
    return mj_create(buf, [
        num_rows,
        num_cols
    ]);
}
function mc_compute_shape_length(arg) {
    var shape, length;
    if (arg.length === 0) {
        shape = [
            1,
            1
        ];
        length = 1;
    } else if (arg.length === 1) {
        shape = [
            arg[0],
            arg[0]
        ];
        length = arg[0] * arg[0];
    } else {
        shape = arg;
        length = 1;
        for (var i = 0; i < shape.length; ++i)
            length *= arg[i];
    }
    return [
        shape,
        length
    ];
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
    var mat = mj_create(buf, [
            rows,
            cols
        ]);
    for (var i = 0; i < rows; ++i) {
        mj_set(mat, [
            i,
            i
        ], 1);
    }
    return mat;
}