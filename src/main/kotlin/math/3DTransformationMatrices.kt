package math

import data.Matrix4x4
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

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

fun projectionMatrix(width: Int, height: Int, fov: Float): Matrix4x4 {
    val cx = width / 2f
    val cy = height / 2f
    val fovRadians = Math.toRadians(fov.toDouble() / 2.0).toFloat()
    val s = cy / tan(fovRadians)

    return Matrix4x4(
        floatArrayOf(s, 0f, cx, 0f),
        floatArrayOf(0f, s, cy, 0f),
        floatArrayOf(0f, 0f, 0f, 1f),
        floatArrayOf(0f, 0f, 1f, 0f),
    )
}
