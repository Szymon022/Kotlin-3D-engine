package math

import data.Float4
import data.Matrix4x4

operator fun Matrix4x4.times(v: Float4): Float4 {
    val (x, y, z, w) = v
    val m = this
    return Float4(
        x = m[0, 0] * x + m[0, 1] * y + m[0, 2] * z + m[0, 3] * w,
        y = m[1, 0] * x + m[1, 1] * y + m[1, 2] * z + m[1, 3] * w,
        z = m[2, 0] * x + m[2, 1] * y + m[2, 2] * z + m[2, 3] * w,
        w = m[3, 0] * x + m[3, 1] * y + m[3, 2] * z + m[3, 3] * w,
    )
}

operator fun Matrix4x4.times(b: Matrix4x4): Matrix4x4 {
    val a = this
    val result = arrayOf(
        floatArrayOf(0f, 0f, 0f, 0f),
        floatArrayOf(0f, 0f, 0f, 0f),
        floatArrayOf(0f, 0f, 0f, 0f),
        floatArrayOf(0f, 0f, 0f, 0f),
    )
    for (i in 0 until 4) {
        for (j in 0 until 4) {
            for (k in 0 until 4) {
                result[i][j] += a[i, k] * b[k, j]
            }
        }
    }
    return Matrix4x4(result[0], result[1], result[2], result[3])
}
