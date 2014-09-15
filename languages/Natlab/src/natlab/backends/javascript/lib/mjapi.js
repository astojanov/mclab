/*
 * MatJuice API.  These functions are used internally in the library
 * for MATLAB built-ins, however they do not correspond to any MATLAB
 * built-in.
 */


/* Create a new matrix object.
 * x    : the Float64Array that contains the data.
 * shape: an array containing the dimensions of the matrix.
 *
 * Note: the elements in x are expected to be in column-major order,
 *       i.e. the matrix [1 2 ; 3 4] is represented as {1, 3, 2, 4}.
 */
function mj_create(x, shape) {
    var make_stride = function(shape, dims) {
        var stride = [1];

        for (var i = 0; i < dims - 1; ++i) {
            stride.push(stride[i] * shape[i]);
        }
        return stride;
    }

    if (typeof x === "object") {
        x.mj_length = x.length;
        x.mj_shape = shape;
        x.mj_dims = shape.length;
        x.mj_stride = make_stride(shape, x.mj_dims);
    }
    return x;
}


function mj_clone(x) {
    if (mj_scalar(x)) {
        return x;
    }
    else {
        var newbuf = new Float64Array(x);
        var newshape = mj_shape(x).slice(0);
        return mj_create(newbuf, newshape);
    }
}


function mj_scalar(x) {
    return typeof x === "number";
}


function mj_length(x) {
    return x.mj_length || 1;
}

function mj_shape(x) {
    return x.mj_shape || [1,1];
}

function mj_stride(x) {
    return x.mj_stride || [1];
}


function mj_dims(x) {
    return x.mj_dims || 2;
}


function mj_compute_index(x, indices) {
    var array_index = 0;
    for (var i = 0, end = indices.length; i < end; ++i) {
        array_index += indices[i] * mj_stride(x)[i];
    }
    return array_index;
}

function mj_get(x, indices) {
    return x[mj_compute_index(x, indices)];
}


function mj_set(x, indices, value) {
    x[mj_compute_index(x, indices)] = value;
}
