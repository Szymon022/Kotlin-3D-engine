package data

import kotlin.math.cos
import kotlin.math.sin

@JvmInline
value class Matrix4x4 private constructor(private val data: Array<FloatArray>) {

    constructor(
        row1: FloatArray,
        row2: FloatArray,
        row3: FloatArray,
        row4: FloatArray
    ) : this(arrayOf(row1, row2, row3, row4))

    operator fun get(i: Int, j: Int) = data[i][j]
}

fun xRotationMatrix(degrees: Float): Matrix4x4 {
    val rad = Math.toRadians(degrees.toDouble()).toFloat()
    val cos = cos(rad)
    val sin = sin(rad)
    return Matrix4x4(
        floatArrayOf(1f, 0f, 0f, 0f),
        floatArrayOf(0f, cos, -sin, 0f),
        floatArrayOf(0f, sin, cos, 0f),
        floatArrayOf(0f, 0f, 0f, 1f),
    )
}

fun yRotationMatrix(degrees: Float): Matrix4x4 {
    val rad = Math.toRadians(degrees.toDouble()).toFloat()
    val cos = cos(rad)
    val sin = sin(rad)
    return Matrix4x4(
        floatArrayOf(cos, 0f, sin, 0f),
        floatArrayOf(0f, 1f, 0f, 0f),
        floatArrayOf(-sin, 0f, cos, 0f),
        floatArrayOf(0f, 0f, 0f, 1f),
    )
}

fun zRotationMatrix(degrees: Float): Matrix4x4 {
    val rad = Math.toRadians(degrees.toDouble()).toFloat()
    val cos = cos(rad)
    val sin = sin(rad)
    return Matrix4x4(
        floatArrayOf(cos, -sin, 0f, 0f),
        floatArrayOf(sin, cos, 0f, 0f),
        floatArrayOf(0f, 0f, 1f, 0f),
        floatArrayOf(0f, 0f, 0f, 1f),
    )
}

fun translationMatrix(x: Float, y: Float, z: Float) = Matrix4x4(
    floatArrayOf(1f, 0f, 0f, x),
    floatArrayOf(0f, 1f, 0f, y),
    floatArrayOf(0f, 0f, 1f, z),
    floatArrayOf(0f, 0f, 0f, 1f),
)

fun scaleMatrix(factor: Float) = Matrix4x4(
    floatArrayOf(factor, 0f, 0f, 0f),
    floatArrayOf(0f, factor, 0f, 0f),
    floatArrayOf(0f, 0f, factor, 0f),
    floatArrayOf(0f, 0f, 0f, 1f),
)
