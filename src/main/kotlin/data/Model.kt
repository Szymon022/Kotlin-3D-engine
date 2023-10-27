package data

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix

data class Model(
    val faces: Array<Face>,
    val color: Color,
    val modelMatrix: Matrix = Matrix(),
    private val onMatrixUpdate: (Float) -> Matrix = { modelMatrix },
) {

    fun update(time: Float) {
        modelMatrix.setFrom(onMatrixUpdate(time))
    }
}
