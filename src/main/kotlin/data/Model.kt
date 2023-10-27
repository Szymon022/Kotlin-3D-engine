package data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix

class Model private constructor(
    val faces: Array<Face>,
    val color: Color,
    val matrix: Matrix,
    private val onMatrixUpdate: (Float) -> Matrix,
) {

    constructor(faces: Array<Face>, color: Color, onMatrixUpdate: (Float) -> Matrix) : this(
        faces = faces,
        color = color,
        onMatrixUpdate = onMatrixUpdate,
        matrix = onMatrixUpdate(0f),
    )

    fun update(time: Float) {
        matrix.setFrom(onMatrixUpdate(time))
    }
}
