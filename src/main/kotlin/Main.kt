import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
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
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.size(width = MainViewModel.WIDTH.dp, height = MainViewModel.HEIGHT.dp)) {
                    Scene(modifier = Modifier.fillMaxSize(), bitmap = scene)
                    if (isLoadingResources) {
                        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    }
                }
                SidePanel(
                    modifier = Modifier.fillMaxHeight().width(200.dp),
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
fun SidePanel(viewModel: MainViewModel, modifier: Modifier = Modifier) {
    val fog by viewModel.fog.collectAsState()
    Column(modifier = modifier) {
        LabeledGroup(label = "Fog controls") {
            Text(text = "minRange: ${String.format("%.3f", fog.minRange)}")
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = fog.minRange,
                onValueChange = viewModel::onMinFogRangeChange,
                valueRange = 0f..3f
            )
            Spacer(Modifier.height(4.dp))
            Text(text = "maxRange: ${String.format("%.3f", fog.maxRange)}")
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = fog.maxRange,
                onValueChange = viewModel::onMaxFoxRangeChange,
                valueRange = 0f..3f
            )
        }
        Divider(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun LabeledGroup(label: String, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit = {}) {
    Column(modifier = modifier) {
        Text(text = label)
        Box(modifier = Modifier.height(4.dp))
        content()
    }
}
