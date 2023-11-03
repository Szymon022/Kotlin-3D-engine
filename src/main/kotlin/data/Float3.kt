package data

import kotlin.math.sqrt

data class Float3(
    val x: Float,
    val y: Float,
    val z: Float
) {

    val length: Float get() = sqrt(x * x + y * y + z * z)

    companion object {
        fun zero() = Float3(0f, 0f, 0f)
        fun unitZ() = Float3(0f, 0f, 1f)
    }
}
