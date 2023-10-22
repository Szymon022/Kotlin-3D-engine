package draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import data.Face
import data.Float3
import data.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import light.drawStillTriangle
import java.awt.image.BufferedImage
import kotlin.system.measureTimeMillis

@OptIn(ExperimentalCoroutinesApi::class)
fun drawModelStill(
    width: Int,
    height: Int,
    model: Model,
    lightColor: Color,
    objColor: Color,
    light: Float3,
    observer: Float3
): Flow<ImageBitmap> = flow {
    measureTimeMillis {
        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        val scale = height / 2
        model.array.asFlow().flatMapMerge(concurrency = 8) {
            drawTriangleStillFlow(
                bitmap = bufferedImage,
                face = it,
                scale = scale,
                lightColor = lightColor,
                objColor = objColor,
                light = light,
                observer = observer,
            )
        }.flowOn(Dispatchers.Default).collect()
        emit(bufferedImage.toComposeImageBitmap())
    }.also { println("rendered in $it ms") }
}.flowOn(Dispatchers.Default)

fun drawTriangleStillFlow(
    bitmap: BufferedImage,
    face: Face,
    scale: Int,
    lightColor: Color,
    objColor: Color,
    light: Float3,
    observer: Float3,
    kd: Float = 1f,
    ks: Float = 1f,
    m: Int = 10,
) = flow {

    bitmap.drawStillTriangle(
        face = face.translateAndScale(scale),
        kd = kd,
        ks = ks,
        m = m,
        lightColor = lightColor,
        objColor = objColor,
        light = light,
        observer = observer,
    )
    emit(Unit)
}
