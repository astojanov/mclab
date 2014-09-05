/*
 * MatJuice API.  These functions are used internally in the library
 * for MATLAB built-ins, however they do not correspond to any MATLAB
 * built-in.
 */

function mj_create(x, shape) {

    var make_stride = function(shape, dims) {
        var stride = [1];
        // for (var i = shape.length - 1, j = 1; i > 0; --i, ++j) {
        //     stride.unshift(stride[stride.length - j] * shape[i]);
        // }

        for (var i = dims, j = 1; i > 0; --i, --j) {
            stride.unshift(stride[stride.length - j] * shape[i]);
        }
        return stride;
    }

    var count_dims = function(shape) {
        var dims = 1;
        for (var i = 0; i < shape.length; ++i) {
            if (shape[i] !== 1)
                dims += 1;
        }
        return dims;
    }

    if (typeof x === "number") {
        x.mj_length = 1;
        x.mj_shape = [1, 1];
        x.mj_dims = 1;
        x.mj_scalar = true;
        x.mj_stride = [1];
    }
    else {
        x.mj_length = x.length;
        x.mj_shape = shape;
        x.mj_dims = count_dims(shape);
        x.mj_scalar = false;
        x.mj_stride = make_stride(shape, x.md_dims);
    }
    return x;
}


function mj_clone(x) {
    if (x.mj_scalar) {
        return x;
    }
    else {
        var newbuf = new Float64Array(x);
        var newshape = x.mj_shape.slice(0);
        return mj_create(newbuf, newshape);
    }
}




function mj_dims(x) {
    return x.mj_shape.length;
}


function mj_get(x, indices) {
    var array_index = 0;
    for (var i = 0, end = indices.length; i < end; ++i) {
        array_index += indices[i] * x.mj_stride[i];
    }
    return x[array_index];
}


function mj_set(x, indices, value) {
    var array_index = 0;
    for (var i = 0, end = indices.length; i < end; ++i) {
        array_index += indices[i] * x.mj_stride[i];
    }
    x[array_index] = value;
}
