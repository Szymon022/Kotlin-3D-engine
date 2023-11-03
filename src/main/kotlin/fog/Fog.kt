package fog

import androidx.compose.ui.graphics.Color

data class Fog(
    val minRange: Float,
    val maxRange: Float,
    val color: Color = Color.White
)
