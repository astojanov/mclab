function plus(x, y) {
    return x+y;
}

function minus(x, y) {
    return x-y;
}

function uminus(x) {
    return -x;
}

function gt(x, y) {
    return x>y;
}

function lt(x, y) {
    return x < y;
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


function array_copy(x) {
    return x.concat();
}

function size(m) {
    return [[m.length, m[0].length]];
}
