package data

import androidx.compose.ui.graphics.Matrix
import math.createPerspectiveFieldOfView
import math.lookAt
import math.transform

class Camera private constructor(
    val position: Float3,
    val lookAt: Matrix = Matrix(),
    val target: Float3,
    val perspective: Matrix = Matrix(),
    private val owner: Model? = null
) {

    companion object {

        fun create(
            relativePosition: Float3,
            relativeTarget: Float3,
            upVector: Float3,
            fov: Float,
            width: Int,
            height: Int,
            owner: Model? = null
        ): Camera {
            val perspective = createPerspectiveFieldOfView(fov, width / height.toFloat(), 1f, 5f)
            return if (owner != null) {
                val modelMatrix = owner.matrix
                val position = relativePosition.transform(modelMatrix)
                val target = Float3.zero().transform(modelMatrix)
                val lookAt = lookAt(position, target, upVector)
                Camera(position, lookAt, target, perspective, owner)
            } else {
                val lookAt = lookAt(relativePosition, relativeTarget, upVector)
                Camera(relativePosition, lookAt, relativeTarget, perspective, null)
            }
        }
    }
}
