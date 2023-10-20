package math

import data.Float3

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
