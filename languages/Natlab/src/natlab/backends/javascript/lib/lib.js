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
function mc_array_set(m, indices, value) {
    return mj_set(m, indices, value);
}
function mc_horzcat() {
    return mj_create(arguments, [
        1,
        arguments.length
    ]);
}
function mc_vertcat() {
    return mj_create(arguments, [
        arguments.length,
        1
    ]);
}
function mc_randn(m, n) {
    var buf = new Float64Array(m * n);
    for (var i = 0; i < m * n; ++i) {
        buf[i] = Math.random();
    }
    return mj_create(buf, [
        m,
        n
    ]);
}