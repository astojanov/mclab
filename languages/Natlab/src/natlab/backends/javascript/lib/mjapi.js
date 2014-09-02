/*
 * MatJuice API.  These functions are used internally in the library
 * for MATLAB built-ins, however they do not correspond to any MATLAB
 * built-in.
 */

function MJ_create(x, size) {
    if (typeof x === "number")
        return x;
    else {
        return {
            data: x,
            size: size
        };
    }
}


function MJ_clone(x) {
    if (typeof x === "number")
        return x;
    else {
        var newbuf = new Float64Array(x.data);
        var newsize = MJ_getSize(x).slice(0);
        return MJ_create(newbuf, newsize);
    }
}


function MJ_clone_meta(x) {
    if (typeof x === "number")
        return x;
    else {
        var newbuf = new Float64Array(MJ_length(x));
        var newsize = MJ_getSize(x).slice(0);
        return MJ_create(newbuf, newsize);
    }
}


function MJ_getSize(x) {
    if (typeof x === "number")
        return [1, 1];
    else
        return x.size;
}


function MJ_length(x) {
    var p = 1;
    var size = MJ_getSize(x);
    for (var i = 0, N = MJ_getDims(x); i < N; ++i) {
        p *= size[i];
    }
    return p;
}


function MJ_getDims(x) {
    return MJ_getSize(x).length;
}


function MJ_getElem(x, idx) {
    if (typeof x === "number") {
        if (idx === 0)
            return x;
        else
            throw "Index out of bounds";
    }
    else {
        return x.data[idx]
    }
}


function MJ_setElem(x, idx, value) {
    // VFB: we should probably use static analysis to avoid calling
    // this on scalars.
    if (typeof x === "number") {
        if (idx === 0)
            ;
        else
            throw "Index out of bounds";
    }
    else {
        x.data[idx] = value;
    }
}
