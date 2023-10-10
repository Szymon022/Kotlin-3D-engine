import androidx.compose.ui.graphics.ImageBitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel : ViewModel() {

    private val _scene = MutableStateFlow(initialScene())
    val scene = _scene.asStateFlow()

    private fun initialScene() = ImageBitmap(WIDTH, HEIGHT)

    companion object {
        const val WIDTH = 900
        const val HEIGHT = 600
    }
}
