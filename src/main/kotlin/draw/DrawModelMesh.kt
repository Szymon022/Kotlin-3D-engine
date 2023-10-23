package draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import data.Face
import data.Float4
import data.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

fun drawModelMesh(width: Int, height: Int, model: Model, color: Color = Color.Black): Flow<ImageBitmap> = flow {
    val image = ImageBitmap(width = width, height = height)
    val paint = Paint().apply { this.color = color }
    val scale = height / 2
    Canvas(image).apply {
        model.faces.forEach {
            drawFaceBorder(face = it.translateAndScale(scale), paint = paint)
        }
    }
    emit(image)
}.flowOn(Dispatchers.Default)

fun Canvas.drawFaceBorder(face: Face, paint: Paint) {
    val (vertices, _) = face
    val size = vertices.size
    repeat(size) { i ->
        val (x1, y1) = vertices[i % size]
        val (x2, y2) = vertices[(i + 1) % size]
        drawLine(
            p1 = Offset(x1, y1),
            p2 = Offset(x2, y2),
            paint = paint
        )
    }
}

fun Face.translateAndScale(scale: Int) = copy(
    vertices = vertices.map { (x, y, z, w) ->
        Float4(
            x = (x + 1) * scale,
            y = (y + 1) * scale,
            z = (z + 1) * scale,
            w = w,
        )
    }.toTypedArray()
)
