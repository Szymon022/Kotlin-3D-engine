package draw

import ShadingParams
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import culling.ZBuffer
import culling.isVisible
import data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import light.drawTriangleGouraud
import java.awt.image.BufferedImage

@OptIn(ExperimentalCoroutinesApi::class)
fun drawModelGouraud(
    bufferedImage: BufferedImage,
    zBuffer: ZBuffer,
    model: Model,
    shadingParams: ShadingParams,
    lightColor: Color,
    light: Float3,
    camera: Camera,
): Flow<ImageBitmap> = flow {
    model.faces.asFlow().flatMapMerge(concurrency = 1) { face ->
        toCanvasFaceFlow(
            canvas = bufferedImage,
            face = face,
            model = model.matrix,
            lookAt = camera.lookAt,
            perspective = camera.perspective,
        )
    }.filter { it.isVisible(camera.target) }
        .onEach { canvasFace ->
            bufferedImage.drawTriangleGouraud(
                face = canvasFace,
                zBuffer = zBuffer,
                kd = shadingParams.kd,
                ks = shadingParams.ks,
                m = shadingParams.m,
                lightColor = lightColor,
                objColor = model.color,
                light = light,
                camera = camera,
            )
        }.flowOn(Dispatchers.Default)
        .collect()
    emit(bufferedImage.toComposeImageBitmap())
}.flowOn(Dispatchers.Default)