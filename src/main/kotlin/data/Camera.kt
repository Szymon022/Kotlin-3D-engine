package data

import androidx.compose.ui.graphics.Matrix
import math.createPerspectiveFieldOfView
import math.lookAt
import math.transform

class Camera private constructor(
    val position: Float3,
    val lookAt: Matrix = Matrix(),
    val perspective: Matrix = Matrix(),
    private val owner: Model? = null
) {

    companion object {

        fun create(
            relativePosition: Float3,
            relativeTarget: Float3,
            fov: Float,
            width: Int,
            height: Int,
            owner: Model? = null
        ): Camera {
            val perspective = createPerspectiveFieldOfView(fov, height / width.toFloat(), 1f, 3f)
            return if (owner != null) {
                val modelMatrix = owner.matrix
                val position = relativePosition.transform(modelMatrix)
                val target = Float3.zero().transform(modelMatrix)
                val lookAt = lookAt(position, target, Float3.unitZ())
                Camera(position, lookAt, perspective, owner)
            } else {
                val lookAt = lookAt(relativePosition, relativeTarget, Float3.unitZ())
                Camera(relativePosition, lookAt, perspective, null)
            }
        }
    }
}
