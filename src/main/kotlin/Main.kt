import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    Window(
        state = WindowState(
            size = DpSize(
                (ViewModel.WIDTH + 200).dp,
                ViewModel.HEIGHT.dp + 40.dp
            ),
        ),
        onCloseRequest = ::exitApplication

    ) {
        MaterialTheme {
            val viewModel = remember { ViewModel() }
            val scene by viewModel.scene.collectAsState()
            Scene(
                modifier = Modifier.size(width = ViewModel.WIDTH.dp, height = ViewModel.HEIGHT.dp),
                bitmap = scene
            )
        }
    }
}
