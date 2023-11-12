import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.graphics.toComposeImageBitmap
import culling.ZBuffer
import data.Camera
import data.Float3
import data.Model
import draw.drawModelPhong
import draw.reset
import fog.Fog
import fog.addFog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import math.normalize
import math.transpose
import obj.parseObj
import viewModel.ViewModel
import java.awt.image.BufferedImage

class MainViewModel : ViewModel() {

    private val _scene = MutableSharedFlow<ImageBitmap>(replay = 1)
    val scene = _scene.asSharedFlow()

    private val _isLoadingResources = MutableStateFlow(false)
    val isLoadingResources = _isLoadingResources.asStateFlow()

    private val ufoModel = MutableStateFlow<Model?>(null)
    private val ludzikModel = MutableStateFlow<Model?>(null)
    private val camera = MutableStateFlow<Camera?>(null)
    val fog = MutableStateFlow(Fog(.75f, .9f))
    val shading = MutableStateFlow(Shading.Phong)
    val timeOfTheDay = MutableStateFlow(TimeOfTheDay.Day)
    val shadingParams = MutableStateFlow(ShadingParams())

    val lightColor = Color.White
    val light = Float3(0f, 1f, 0f).normalize()
    var time = 0f

    private val zBuffer = ZBuffer(WIDTH, HEIGHT)
    private val bufferedImage = BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB)

    init {
        viewModelScope.launch {
            _isLoadingResources.update { true }
            ufoModel.update {
                Model(
                    faces = parseObj("D:\\IdeaProjects\\Engine3D\\src\\main\\resources\\models\\ufo.obj"),
                    color = Color.Red,
                    onMatrixUpdate = {
                        val translationMatrix = Matrix().apply { translate(0f, 0f, -2f) }
                        val rotationMatrix = Matrix().apply { rotateY(it) }
                        val secondTranslationMatrix = Matrix().apply { translate(0f, 0f, -9f) }
                        translationMatrix.timesAssign(rotationMatrix)
                        translationMatrix.timesAssign(secondTranslationMatrix)
                        translationMatrix.transpose()
                        translationMatrix
                    }
                )
            }
            ludzikModel.update {
                Model(
                    faces = parseObj("D:\\IdeaProjects\\Engine3D\\src\\main\\resources\\models\\ludzik.obj"),
                    color = Color.Cyan,
                    onMatrixUpdate = {
                        val rotationMatrix = Matrix().apply { rotateX(it) }
                        val translationMatrix = Matrix().apply { translate(0f, 0f, -10f) }
                        rotationMatrix.timesAssign(translationMatrix)
                        rotationMatrix.transpose()
                        rotationMatrix
                    }
                )
            }
            camera.update {
                Camera.create(
                    relativePosition = Float3(0f, 0f, -15f),
                    relativeTarget = Float3(0f, 0f, 5f),
                    upVector = Float3(0f, 1f, 0f),
                    fov = 60f,
                    width = WIDTH,
                    height = HEIGHT,
//                    owner = ludzikModel.value!!
                )
            }
            delay(500)
            _isLoadingResources.update { false }
            while (true) {
                zBuffer.reset()
                bufferedImage.reset(if (timeOfTheDay.value == TimeOfTheDay.Day) Color.White else Color.Black)

                drawModelPhong(
                    bufferedImage = bufferedImage,
                    zBuffer = zBuffer,
                    model = ufoModel.value!!,
                    lightColor = lightColor,
                    light = light,
                    camera = camera.value!!,
                ).collect()

                drawModelPhong(
                    bufferedImage = bufferedImage,
                    zBuffer = zBuffer,
                    model = ludzikModel.value!!,
                    lightColor = lightColor,
                    light = light,
                    camera = camera.value!!,
                ).collect()

                bufferedImage.addFog(zBuffer, fog = fog.value)

                _scene.emit(bufferedImage.toComposeImageBitmap())
                time += 1
                ufoModel.value?.update(time)
                ludzikModel.value?.update(time)
//                camera.value?.refresh()
            }
        }
    }

    fun initialScene() = ImageBitmap(WIDTH, HEIGHT)

    fun onMinFogRangeChange(minRange: Float) {
        fog.update {
            it.copy(minRange = minRange)
        }
    }

    fun onMaxFoxRangeChange(maxRange: Float) {
        fog.update {
            it.copy(maxRange = maxRange)
        }
    }

    fun onShadingChange(newShading: Shading) {
        shading.update { newShading }
    }

    fun setTimeOfTheDay(timeOfTheDay: TimeOfTheDay) {
        this.timeOfTheDay.update { timeOfTheDay }
    }

    fun onSetKs(ks: Float) = shadingParams.update { it.copy(ks = ks) }

    fun onSetKd(kd: Float) = shadingParams.update { it.copy(kd = kd) }

    fun onSetM(m: Float) = shadingParams.update { it.copy(m = m.toInt()) }

    companion object {
        const val WIDTH = 900
        const val HEIGHT = 600
    }
}

enum class Shading {
    Still, Gouraud, Phong,
}

enum class TimeOfTheDay {
    Day, Night
}

data class ShadingParams(
    val ks: Float = .6f,
    val kd: Float = .8f,
    val m: Int = 8,
)
