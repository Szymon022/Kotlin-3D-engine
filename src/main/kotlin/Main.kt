import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

@Composable
@Preview
fun Scene(bitmap: ImageBitmap, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        drawImage(bitmap)
    }
}

fun main() = application {
    val viewModel = remember { MainViewModel() }
    Window(
        state = WindowState(
            size = DpSize(
                (MainViewModel.WIDTH + 200).dp,
                MainViewModel.HEIGHT.dp + 40.dp
            ),
        ),
        onCloseRequest = {
            viewModel.onClear()
            exitApplication()
        }
    ) {
        MaterialTheme {
            val scene by viewModel.scene.collectAsState(viewModel.initialScene())
            val isLoadingResources by viewModel.isLoadingResources.collectAsState()
            Box(modifier = Modifier.size(width = MainViewModel.WIDTH.dp, height = MainViewModel.HEIGHT.dp)) {
                Scene(modifier = Modifier.fillMaxSize(), bitmap = scene)
                if (isLoadingResources) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            }
        }
    }
}
