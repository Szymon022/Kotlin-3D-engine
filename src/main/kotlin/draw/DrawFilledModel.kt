package draw

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import data.Face
import data.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlin.system.measureTimeMillis

@OptIn(ExperimentalCoroutinesApi::class)
fun drawFilledModel(width: Int, height: Int, model: Model): Flow<ImageBitmap> = flow {
    measureTimeMillis {
        val image = ImageBitmap(width = width, height = height)
        val paint = Paint().apply { color = Color.Blue }
        val scale = height / 2
        Canvas(image).apply {
            model.array.asFlow().flatMapMerge(concurrency = 4) {
                drawFilledPolygonFlow(this, it, scale, paint)
            }.flowOn(Dispatchers.Default).collect()
        }
        emit(image)
    }.also { println("rendered in $it ms") }
}.flowOn(Dispatchers.Default)

fun drawFilledPolygonFlow(canvas: Canvas, face: Face, scale: Int, paint: Paint) = flow<Unit> {
    canvas.drawFilledPolygon(face.translateAndScale(scale), paint = paint)
}