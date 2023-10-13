package data

import kotlin.math.sqrt

data class Float3(
    val x: Float,
    val y: Float,
    val z: Float
) {

    val length: Float get() = sqrt(x * x + y * y + z * z)
}
