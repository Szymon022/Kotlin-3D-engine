package math

import androidx.compose.ui.graphics.Matrix
import kotlin.math.tan

fun createPerspectiveFieldOfView(
    fovAngle: Float,
    aspectRatio: Float,
    nearPlaneDistance: Float,
    farPlaneDistance: Float
): Matrix {
    val fovRadians = fovAngle * 3.141592f / 180f
    val cx = 1f / tan(fovRadians / 2f)
    val cy = cx / aspectRatio
    val (n, f) = nearPlaneDistance to farPlaneDistance
    val common = f.toDouble() / (n.toDouble() - f.toDouble())
    return Matrix(
        floatArrayOf(
            cx, 0f, 0f, 0f,
            0f, cy, 0f, 0f,
            0f, 0f, common.toFloat(), -1f,
            0f, 0f, (n.toDouble() * common).toFloat(), 0f,
        )
    )
}
