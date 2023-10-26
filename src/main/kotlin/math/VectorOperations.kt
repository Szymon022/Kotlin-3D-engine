package math

import androidx.compose.ui.graphics.Matrix
import data.Float3
import data.Float4

infix fun Float3.x(w: Float3): Float3 {
    val (a1, a2, a3) = this
    val (b1, b2, b3) = w
    return Float3(
        x = a2 * b3 - a3 * b2,
        y = a3 * b1 - a1 * b3,
        z = a1 * b2 - a2 * b1,
    )
}

infix fun Float3.o(w: Float3) = x * w.x + y * w.y + z * w.z

fun Float3.normalize(): Float3 {
    val length = this.length
    return copy(
        x = x / length,
        y = y / length,
        z = z / length,
    )
}

operator fun Float.times(v: Float3): Float3 {
    val (x, y, z) = v
    return Float3(
        x = x * this,
        y = y * this,
        z = z * this
    )
}

operator fun Float3.plus(v: Float3): Float3 {
    val (ux, uy, uz) = this
    val (vx, vy, vz) = v
    return Float3(
        x = ux + vx,
        y = uy + vy,
        z = uz + vz
    )
}

operator fun Float3.minus(v: Float3): Float3 {
    val (ux, uy, uz) = this
    val (vx, vy, vz) = v
    return Float3(
        x = ux - vx,
        y = uy - vy,
        z = uz - vz
    )
}

fun Float4.transform(m: Matrix) = Float4(
    x = x * m[0, 0] + y * m[0, 1] + z * m[0, 2] + w * m[0, 3],
    y = x * m[1, 0] + y * m[1, 1] + z * m[1, 2] + w * m[1, 3],
    z = x * m[2, 0] + y * m[2, 1] + z * m[2, 2] + w * m[2, 3],
    w = x * m[3, 0] + y * m[3, 1] + z * m[3, 2] + w * m[3, 3],
)

fun Float3.transform(m: Matrix) = Float3(
    x = x * m[0, 0] + y * m[0, 1] + z * m[0, 2],
    y = x * m[1, 0] + y * m[1, 1] + z * m[1, 2],
    z = x * m[2, 0] + y * m[2, 1] + z * m[2, 2],
)

fun Float3.transformNormal(modelMatrix: Matrix): Float3 {
    val m = modelMatrix.clone().apply {
        invert()
        transpose()
    }
    return Float3(
        x = x * m[0, 0] + y * m[0, 1] + z * m[0, 2],
        y = x * m[1, 0] + y * m[1, 1] + z * m[1, 2],
        z = x * m[2, 0] + y * m[2, 1] + z * m[2, 2],
    )
}

fun Matrix.clone() = Matrix(values.clone())

fun Matrix.transpose() {
    for (i in 0..3) {
        for (j in i..3) {
            val t = this[i, j]
            this[i, j] = this[j, i]
            this[j, i] = t
        }
    }
}
