package draw

import ShadingParams
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import culling.ZBuffer
import culling.isVisible
import data.Camera
import data.CanvasFace
import data.Float3
import data.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import light.drawTrianglePhong
import java.awt.image.BufferedImage
import kotlin.system.measureTimeMillis

@OptIn(ExperimentalCoroutinesApi::class)
fun drawModelPhong(
    bufferedImage: BufferedImage,
    zBuffer: ZBuffer,
    model: Model,
    shadingParams: ShadingParams,
    lightColor: Color,
    light: Float3,
    camera: Camera,
): Flow<ImageBitmap> = flow {
    measureTimeMillis {
        model.faces.asFlow().flatMapMerge { face ->
            toCanvasFaceFlow(
                canvas = bufferedImage,
                face = face,
                model = model.matrix,
                lookAt = camera.lookAt,
                perspective = camera.perspective,
            )
        }.filter { it.isVisible(camera.target) }
            .flatMapMerge(concurrency = 8) {
                drawTrianglePhongFlow(
                    bitmap = bufferedImage,
                    zBuffer = zBuffer,
                    face = it,
                    lightColor = lightColor,
                    objColor = model.color,
                    light = light,
                    camera = camera,
                    kd = shadingParams.kd,
                    ks = shadingParams.ks,
                    m = shadingParams.m
                )
            }.flowOn(Dispatchers.Default).collect()
        emit(bufferedImage.toComposeImageBitmap())
    }.also { println("rendered in $it ms") }
}.flowOn(Dispatchers.Default)


fun drawTrianglePhongFlow(
    bitmap: BufferedImage,
    zBuffer: ZBuffer,
    face: CanvasFace,
    lightColor: Color,
    objColor: Color,
    light: Float3,
    camera: Camera,
    kd: Float = 1f,
    ks: Float = 1f,
    m: Int = 10,
) = flow {

    bitmap.drawTrianglePhong(
        face = face,
        zBuffer = zBuffer,
        kd = kd,
        ks = ks,
        m = m,
        lightColor = lightColor,
        objColor = objColor,
        light = light,
        camera = camera,
    )
    emit(Unit)
}
