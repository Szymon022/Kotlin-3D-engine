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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import fog.Fog

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
    val shading by viewModel.shading.collectAsState()
    val time by viewModel.timeOfTheDay.collectAsState()
    Column(modifier = modifier) {
        TimeOfTheDay(time = time, onChange = viewModel::setTimeOfTheDay)
        FogControls(
            fog = fog,
            onMinFogRangeChange = viewModel::onMinFogRangeChange,
            onMaxFogRangeChange = viewModel::onMaxFoxRangeChange
        )
        Divider(modifier = Modifier.fillMaxWidth())
        ShadingType(shadingAlgorithm = shading, onShadingAlgorithmChange = viewModel::onShadingChange)
    }
}

@Composable
fun LabeledGroup(label: String, modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit = {}) {
    Column(modifier = modifier) {
        Text(text = label, fontWeight = FontWeight.SemiBold)
        Box(modifier = Modifier.height(4.dp))
        content()
    }
}

@Composable
fun TimeOfTheDay(time: TimeOfTheDay, onChange: (TimeOfTheDay) -> Unit) {
    LabeledGroup("Time of the day") {
        Row {
            TimeOfTheDay.values().forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = it == time, onCheckedChange = { isChecked -> if (isChecked) onChange(it) })
                    Text(it.name)
                }
            }
        }
    }
}

@Composable
fun FogControls(
    fog: Fog,
    onMinFogRangeChange: (Float) -> Unit,
    onMaxFogRangeChange: (Float) -> Unit,
) {
    LabeledGroup(label = "Fog controls") {
        Text(text = "minRange: ${String.format("%.3f", fog.minRange)}", style = MaterialTheme.typography.caption)
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = fog.minRange,
            onValueChange = onMinFogRangeChange,
            valueRange = 0f..3f
        )
        Spacer(Modifier.height(4.dp))
        Text(text = "maxRange: ${String.format("%.3f", fog.maxRange)}", style = MaterialTheme.typography.caption)
        Slider(
            modifier = Modifier.fillMaxWidth(),
            value = fog.maxRange,
            onValueChange = onMaxFogRangeChange,
            valueRange = 0f..3f
        )
    }
}

@Composable
fun ShadingType(shadingAlgorithm: Shading, onShadingAlgorithmChange: (Shading) -> Unit) {
    LabeledGroup("Shading type") {
        Shading.values().forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = it == shadingAlgorithm,
                    onCheckedChange = { checked ->
                        if (checked) {
                            onShadingAlgorithmChange(it)
                        }
                    }
                )
                Text(text = it.name, style = MaterialTheme.typography.caption)
            }
        }
    }
}