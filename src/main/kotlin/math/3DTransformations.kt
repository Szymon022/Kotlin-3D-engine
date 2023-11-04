package math

import androidx.compose.ui.graphics.Matrix
import data.Float3
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
            cy, 0f, 0f, 0f,
            0f, cx, 0f, 0f,
            0f, 0f, common.toFloat(), -1f,
            0f, 0f, (n.toDouble() * common).toFloat(), 0f,
        )
    )
}

fun lookAt(cameraPosition: Float3, cameraTarget: Float3, cameraUpVector: Float3): Matrix {
    val v31 = (cameraPosition - cameraTarget).normalize()
    val v32 = (cameraUpVector x v31).normalize()
    val v1 = v31 x v32
    return Matrix(
        floatArrayOf(
            v32.x, v1.x, v31.x, 0f,
            v32.y, v1.y, v31.y, 0f,
            v32.z, v1.z, v31.z, 0f,
            -(v32 o cameraPosition), -(v1 o cameraPosition), -(v31 o cameraPosition), 1f
        )
    ).also { it.transpose() }
}
