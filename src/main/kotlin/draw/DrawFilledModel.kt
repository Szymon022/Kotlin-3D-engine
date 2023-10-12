package draw

import androidx.compose.ui.graphics.*
import data.Face
import data.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.awt.image.BufferedImage
import kotlin.system.measureTimeMillis

@OptIn(ExperimentalCoroutinesApi::class)
fun drawFilledModel(width: Int, height: Int, model: Model, color: Color): Flow<ImageBitmap> = flow {
    measureTimeMillis {
        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val paint = Paint().apply { this.color = color }
        val scale = height / 2
        model.array.asFlow().flatMapMerge(concurrency = 8) {
            drawFilledPolygonFlow(bufferedImage, it, scale, paint)
        }.flowOn(Dispatchers.Default).collect()
        emit(bufferedImage.toComposeImageBitmap())
    }.also { println("rendered in $it ms") }
}.flowOn(Dispatchers.Default)

fun drawFilledPolygonFlow(canvas: Canvas, face: Face, scale: Int, paint: Paint) = flow<Unit> {
    canvas.drawFilledPolygon(face.translateAndScale(scale), paint = paint)
}

fun drawFilledPolygonFlow(bitmap: BufferedImage, face: Face, scale: Int, paint: Paint) = flow<Unit> {
    bitmap.drawFilledPolygon(face.translateAndScale(scale), color = paint.color.toArgb())
}