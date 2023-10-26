package draw

import androidx.compose.ui.graphics.Matrix
import data.CanvasFace
import data.Face
import data.Float3
import data.Float4
import kotlinx.coroutines.flow.flow
import math.transform
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

private fun Float4.toCanvas(model: Matrix, lookAt: Matrix, perspective: Matrix, width: Int, height: Int) =
    transform(model)
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

private inline fun Float3.isVisible(width: Int, height: Int) = x >= 0 && y >= 0 && x < width && y < height
