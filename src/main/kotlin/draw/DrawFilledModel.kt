package draw

import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import data.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.system.measureTimeMillis

fun drawFilledModel(width: Int, height: Int, model: Model): Flow<ImageBitmap> = flow {
    measureTimeMillis {
        val image = ImageBitmap(width = width, height = height)

        val paint = Paint().apply { color = Color.Blue }
        val scale = height / 2
        Canvas(image).apply {
            model.array.forEach {
                drawFilledPolygon(face = it.translateAndScale(scale), paint = paint)
            }
        }
        emit(image)
    }.also { println("rendered in $it ms") }
}.flowOn(Dispatchers.Default)