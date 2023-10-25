package math

import androidx.compose.ui.graphics.Matrix
import kotlin.math.tan

fun Matrix.projection(width: Int, height: Int, fovAngle: Float): Matrix {
    val cx = width / 2f
    val cy = height / 2f
    val s = cy / tan(fovAngle / 180f * 3.141592f)
    return Matrix(
        floatArrayOf(
            s, 0f, cx, 0f,
            0f, s, cy, 0f,
            0f, 0f, 0f, 1f,
            0f, 0f, 1f, 0f,
        )
    )
}
