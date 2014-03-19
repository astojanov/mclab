function plus_SS(x, y) {
    return x+y;
}


function plus_SM(x, m) {
    var m2 = clone(m);
    for (var i = 0, len1 = length(m2); i < len1; ++i) {
        for (var j = 0, len2 = length(m2[i]); j < len2; ++j) {
            m2[i][j] += x;
        }
    }
    return m2;
}

function plus_MS(m, x) {
    return plus_SM(x, m);
}

function plus_MM(m1, m2) {
    // if (size(m1) !== size(m2)) {
    //     throw "error: the sizes of arg1 and arg2 differ";
    // }

    var r = clone(m1);

    for (var i = 0, len1 = length(m1); i < len1; ++i) {
        for (var j = 0, len2 = length(m1[i]); j < len2; ++j) {
            r[i][j] += m2[i][j];
        }
    }

    return r;
}


function minus(x, y) {
    return x-y;
}

function gt(x, y) {
    return x>y;
}

function length(xs) {
    return xs.length;
}


function clone(x) {
    var y = [];
    for (var key in x) {
        if (typeof x[key] === "object")
            y[key] = clone(x[key]);
        else
            y[key] = x[key];
    }
    return y;
}

function size(m) {
    return [[m.length, m[0].length]];
}
