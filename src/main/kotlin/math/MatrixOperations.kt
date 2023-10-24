package math

import androidx.compose.ui.graphics.Matrix
import data.Float3

fun Matrix.translate(vector: Float3) {
    val (x, y, z) = vector
    this[0, 3] += x
    this[1, 3] += y
    this[2, 3] += z
}
