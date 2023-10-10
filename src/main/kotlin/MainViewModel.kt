import androidx.compose.ui.graphics.ImageBitmap
import data.Model
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import obj.parseObj
import viewModel.ViewModel

class MainViewModel : ViewModel() {

    private val _scene = MutableStateFlow(initialScene())
    val scene = _scene.asStateFlow()

    private val _isLoadingResources = MutableStateFlow(false)
    val isLoadingResources = _isLoadingResources.asStateFlow()

    private val model = MutableStateFlow<Model?>(null)

    init {
        viewModelScope.launch {
            _isLoadingResources.update { true }
            model.update { parseObj("D:\\IdeaProjects\\Engine3D\\src\\main\\resources\\models\\sphere.obj") }
            _isLoadingResources.update { false }
        }
    }

    private fun initialScene() = ImageBitmap(WIDTH, HEIGHT)

    companion object {
        const val WIDTH = 900
        const val HEIGHT = 600
    }
}
