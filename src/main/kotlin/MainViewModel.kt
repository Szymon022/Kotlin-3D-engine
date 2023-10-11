import androidx.compose.ui.graphics.ImageBitmap
import data.Model
import draw.drawModelMesh
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import obj.parseObj
import viewModel.ViewModel

class MainViewModel : ViewModel() {

    private val _scene = MutableSharedFlow<ImageBitmap>(replay = 1)
    val scene = _scene.asSharedFlow()

    private val _isLoadingResources = MutableStateFlow(false)
    val isLoadingResources = _isLoadingResources.asStateFlow()

    private val model = MutableStateFlow<Model?>(null)

    init {
        viewModelScope.launch {
            _isLoadingResources.update { true }
            model.update { parseObj("D:\\IdeaProjects\\Engine3D\\src\\main\\resources\\models\\sphere.obj") }
            _isLoadingResources.update { false }

            drawModelMesh(
                width = WIDTH,
                height = HEIGHT,
                model = model.value!!
            ).collect { bitmap ->
                _scene.emit(bitmap)
            }
        }
    }

    fun initialScene() = ImageBitmap(WIDTH, HEIGHT)

    companion object {
        const val WIDTH = 900
        const val HEIGHT = 600
    }
}
