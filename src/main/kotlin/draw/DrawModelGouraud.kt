package draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.toComposeImageBitmap
import culling.ZBuffer
import culling.isVisible
import data.CanvasFace
import data.Face
import data.Float3
import data.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import light.drawTriangleGouraud
import math.createPerspectiveFieldOfView
import math.lookAt
import math.translate
import java.awt.image.BufferedImage
import kotlin.system.measureTimeMillis

@OptIn(ExperimentalCoroutinesApi::class)
fun drawModelGouraud(
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
        val zBuffer = ZBuffer(width = width, height = height)

        val lookAt = lookAt(
            cameraPosition = Float3(.0f, .0f, 100f),
            cameraTarget = Float3(.0f, .0f, -100f),
            cameraUpVector = Float3(0f, 1f, 0f)
        )
        val perspective = createPerspectiveFieldOfView(
            fovAngle = 60f,
            aspectRatio = height.toFloat() / width,
            nearPlaneDistance = 1f,
            farPlaneDistance = 5f
        )

        model.faces.asFlow().flatMapMerge { face ->
            toCanvasFaceFlow(
                face = face,
                canvas = bufferedImage,
                model = model.matrix,
                lookAt = lookAt,
                perspective = perspective,
            )
        }.filter { it.isVisible(camera = observer) }
            .flatMapMerge(concurrency = 8) {
                drawTriangleGouraudFlow(
                    bitmap = bufferedImage,
                    zBuffer = zBuffer,
                    face = it,
                    lightColor = lightColor,
                    objColor = objColor,
                    light = light,
                    observer = observer,
                )

            }.flowOn(Dispatchers.Default).collect()
        emit(bufferedImage.toComposeImageBitmap())
    }.also { println("rendered in $it ms") }
}.flowOn(Dispatchers.Default)


fun drawTriangleGouraudFlow(
    bitmap: BufferedImage,
    zBuffer: ZBuffer,
    face: CanvasFace,
    lightColor: Color,
    objColor: Color,
    light: Float3,
    observer: Float3,
    kd: Float = 1f,
    ks: Float = 1f,
    m: Int = 10,
) = flow {

    bitmap.drawTriangleGouraud(
        zBuffer = zBuffer,
        face = face,
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

