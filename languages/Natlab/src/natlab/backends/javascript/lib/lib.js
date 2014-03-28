function plus(x, y) {
    return x+y;
}

function minus(x, y) {
    return x-y;
}

function gt(x, y) {
    return x>y;
}

function ne(x, y) {
    return x !== y;
}

function mod(x, y) {
    return x % y;
}

function eq(x, y) {
    return x === y;
}

function mtimes(x, y) {
    return x*y;
}

function mrdivide(x, y) {
    return x / y;
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
