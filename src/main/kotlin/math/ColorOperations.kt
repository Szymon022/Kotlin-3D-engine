package math

import androidx.compose.ui.graphics.Color

operator fun Color.times(x: Float) = Color(
    red = (red * x).coerceIn(0f, 1f),
    green = (green * x).coerceIn(0f, 1f),
    blue = (blue * x).coerceIn(0f, 1f),
)

operator fun Color.plus(color: Color) = Color(
    red = (red + color.red).coerceIn(0f, 1f),
    green = (green + color.green).coerceIn(0f, 1f),
    blue = (blue + color.blue).coerceIn(0f, 1f),
)