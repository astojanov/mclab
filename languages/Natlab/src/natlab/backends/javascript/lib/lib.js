function mc_plus_SS(x, y) {
    return x + y;
}
function mc_plus_SM(x, m) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) + x);
    }
    ;
    return out;
}
function mc_plus_MS(m, x) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) + x);
    }
    ;
    return out;
}
function mc_plus_MM(m1, m2) {
    var out = new MJ_copy(m1);
    var m1_length = MJ_length(m1);
    var m2_length = MJ_length(m2);
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = MJ_getElem(m1, i);
        var y = MJ_getElem(m2, i);
        MJ_setElem(out, i, x + y);
    }
    ;
    return out;
}
function mc_minus_SS(x, y) {
    return x - y;
}
function mc_minus_SM(x, m) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) - x);
    }
    ;
    return out;
}
function mc_minus_MS(m, x) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) - x);
    }
    ;
    return out;
}
function mc_minus_MM(m1, m2) {
    var out = MJ_copy(m1);
    var m1_length = MJ_length(m1);
    var m2_length = MJ_length(m2);
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = MJ_getElem(m1, i);
        var y = MJ_getElem(m2, i);
        MJ_setElem(out, i, x - y);
    }
    ;
    return out;
}
function mc_rem_SS(x, y) {
    return x % y;
}
function mc_rem_SM(x, m) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) % x);
    }
    ;
    return out;
}
function mc_rem_MS(m, x) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) % x);
    }
    ;
    return out;
}
function mc_rem_MM(m1, m2) {
    var out = MJ_copy(m1);
    var m1_length = MJ_length(m1);
    var m2_length = MJ_length(m2);
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = MJ_getElem(m1, i);
        var y = MJ_getElem(m2, i);
        MJ_setElem(out, i, x % y);
    }
    ;
    return out;
}
function mc_mtimes_SS(x, y) {
    return x * y;
}
function mc_mtimes_SM(x, m) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) * x);
    }
    ;
    return out;
}
function mc_mtimes_MS(m, x) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) * x);
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
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) / x);
    }
    ;
    return out;
}
function mc_mrdivide_MS(m, x) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) / x);
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
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) < x);
    }
    ;
    return out;
}
function mc_lt_MS(m, x) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) < x);
    }
    ;
    return out;
}
function mc_lt_MM(m1, m2) {
    var out = MJ_copy(m1);
    var m1_length = MJ_length(m1);
    var m2_length = MJ_length(m2);
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = MJ_getElem(m1, i);
        var y = MJ_getElem(m2, i);
        MJ_setElem(out, i, x < y);
    }
    ;
    return out;
}
function mc_gt_SS(x, y) {
    return x > y;
}
function mc_gt_SM(x, m) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) > x);
    }
    ;
    return out;
}
function mc_gt_MS(m, x) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) > x);
    }
    ;
    return out;
}
function mc_gt_MM(m1, m2) {
    var out = MJ_copy(m1);
    var m1_length = MJ_length(m1);
    var m2_length = MJ_length(m2);
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = MJ_getElem(m1, i);
        var y = MJ_getElem(m2, i);
        MJ_setElem(out, i, x > y);
    }
    ;
    return out;
}
function mc_eq_SS(x1, x2) {
    return x1 === x2;
}
function mc_eq_SM(x, m) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) === x);
    }
    ;
    return out;
}
function mc_eq_MS(m, x) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) === x);
    }
    ;
    return out;
}
function mc_eq_MM(m1, m2) {
    var out = MJ_copy(m1);
    var m1_length = MJ_length(m1);
    var m2_length = MJ_length(m2);
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = MJ_getElem(m1, i);
        var y = MJ_getElem(m2, i);
        MJ_setElem(out, i, x === y);
    }
    ;
    return out;
}
function mc_ne_SS(x1, x2) {
    return x1 !== x2;
}
function mc_ne_SM(x, m) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) !== x);
    }
    ;
    return out;
}
function mc_ne_MS(m, x) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, MJ_getElem(m, i) !== x);
    }
    ;
    return out;
}
function mc_ne_MM(m1, m2) {
    var out = MJ_copy(m1);
    var m1_length = MJ_length(m1);
    var m2_length = MJ_length(m2);
    if (m1_length !== m2_length)
        throw 'array sizes differ';
    for (var i = 0, n = m1_length; i < n; ++i) {
        var x = MJ_getElem(m1, i);
        var y = MJ_getElem(m2, i);
        MJ_setElem(out, i, x !== y);
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
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, Math.sin(MJ_getElem(m, i)));
    }
    ;
    return out;
}
function mc_uminus_S(x) {
    return -x;
}
function mc_uminus_M(m) {
    var out = MJ_copy(m);
    for (var i = 0, N = MJ_length(m); i < N; ++i) {
        MJ_setElem(out, i, function (x) {
            return -x;
        }(MJ_getElem(m, i)));
    }
    ;
    return out;
}