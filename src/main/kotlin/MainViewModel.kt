import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import data.Model
import draw.drawFilledModel
import fog.Fog
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import obj.parseObj
import viewModel.ViewModel

class MainViewModel : ViewModel() {

    private val _scene = MutableSharedFlow<ImageBitmap>(replay = 1)
    val scene = _scene.asSharedFlow()

    private val _isLoadingResources = MutableStateFlow(false)
    val isLoadingResources = _isLoadingResources.asStateFlow()

    val fog = MutableStateFlow(Fog(.75f, .9f))
    val shading = MutableStateFlow(Shading.Phong)

    private val model = MutableStateFlow<Model?>(null)

    private val colors = List(1000000) {
        Color(it * 100000000)
    }

    init {
        viewModelScope.launch {
            _isLoadingResources.update { true }
            model.update { parseObj("D:\\IdeaProjects\\Engine3D\\src\\main\\resources\\models\\sphere.obj") }
            _isLoadingResources.update { false }

            colors.forEach {
                drawFilledModel(
                    width = WIDTH,
                    height = HEIGHT,
                    model = model.value!!,
                    it
                ).collect { bitmap ->
                    _scene.emit(bitmap)
                }
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

    companion object {
        const val WIDTH = 900
        const val HEIGHT = 600
    }
}

enum class Shading {
    Still, Gouraud, Phong,
}
