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
