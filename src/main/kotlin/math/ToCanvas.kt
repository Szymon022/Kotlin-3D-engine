package math

import androidx.compose.ui.graphics.Matrix
import data.Float3
import data.Float4

fun Float4.toCanvas(model: Matrix, lookAt: Matrix, perspective: Matrix, width: Int, height: Int) = transform(model)
    .transform(lookAt)
    .transform(perspective)
    .normalize()
    .toCanvas(
        width = width,
        height = height
    )

private fun Float3.toCanvas(width: Int, height: Int) = copy(
    x = 0.5f * width * (1f + x / 1f),
    y = 0.5f * height * (1f + y / 1f)
)

private fun Float4.normalize() = Float3(x / w, y / w, z / w)
