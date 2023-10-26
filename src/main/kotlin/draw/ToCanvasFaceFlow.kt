package draw

import androidx.compose.ui.graphics.Matrix
import data.CanvasFace
import data.Face
import data.Float3
import kotlinx.coroutines.flow.flow
import math.toCanvas
import math.transformNormal
import java.awt.image.BufferedImage

/**
 * Transforms Face with virtual points to CanvasFace with canvas points and transformed normal vectors.
 */

fun toCanvasFaceFlow(face: Face, canvas: BufferedImage, model: Matrix, lookAt: Matrix, perspective: Matrix) = flow {
    val (vertices, normals) = face
    val canvasVertices = vertices.map {
        val v = it.toCanvas(
            model = model,
            lookAt = lookAt,
            perspective = perspective,
            width = canvas.width,
            height = canvas.height
        )
        if (!v.isVisible(width = canvas.width, height = canvas.height)) return@flow
        v
    }.toTypedArray()
    val transformedNormals = normals.map { it.transformNormal(model) }.toTypedArray()
    emit(CanvasFace(vertices = canvasVertices, normals = transformedNormals))
}

private inline fun Float3.isVisible(width: Int, height: Int) = x >= 0 && y >= 0 && x < width && y < height
