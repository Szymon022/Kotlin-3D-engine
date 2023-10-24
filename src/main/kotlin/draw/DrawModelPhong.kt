package draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.toComposeImageBitmap
import culling.ZBuffer
import culling.isVisible
import data.Face
import data.Float3
import data.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import light.drawTrianglePhong
import math.transform
import math.transformNormal
import math.translate
import java.awt.image.BufferedImage
import kotlin.system.measureTimeMillis

@OptIn(ExperimentalCoroutinesApi::class)
fun drawModelPhong(
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
        val scale = height / 2f
        val zBuffer = ZBuffer(width = width, height = height)

        val modelMatrix = Matrix().apply {
            scale(x = scale, y = scale, z = scale)
            rotateY(35f)
            translate(Float3(x = width / 2f, y = height / 2f, z = 0f))
        }.also { println(it) }

        model.faces.asFlow().flatMapMerge { face ->
            flow {
                emit(
                    Face(
                        vertices = face.vertices.map { it.transform(modelMatrix) }.toTypedArray(),
                        normals = face.normals.map { it.transformNormal(modelMatrix) }.toTypedArray(),
                    )
                )
            }.flowOn(Dispatchers.Default)
        }.filter { it.isVisible(camera = observer) }
            .flatMapMerge(concurrency = 8) {
                drawTrianglePhongFlow(
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


fun drawTrianglePhongFlow(
    bitmap: BufferedImage,
    zBuffer: ZBuffer,
    face: Face,
    lightColor: Color,
    objColor: Color,
    light: Float3,
    observer: Float3,
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
        observer = observer,
    )
    emit(Unit)
}
