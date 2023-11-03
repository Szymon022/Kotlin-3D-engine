package fog

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import culling.ZBuffer
import java.awt.image.BufferedImage

data class Fog(
    val minRange: Float,
    val maxRange: Float,
    val color: Color = Color.White
)

fun BufferedImage.addFog(zBuffer: ZBuffer, fog: Fog) {
    repeat(zBuffer.height) { y ->
        repeat(zBuffer.width) { x ->
            val z = zBuffer[x, y]
            if (z > fog.minRange && z < fog.maxRange) {
                val alpha = (z - fog.minRange) / (fog.maxRange - fog.minRange)
                val rgb = Color(getRGB(x, y))
                setRGB(x, y, blend(rgb, fog.color, alpha).toArgb())
            }
        }
    }
}

private fun blend(color: Color, fogColor: Color, alpha: Float): Color {
    val r = color.red * (1 - alpha) + fogColor.red * alpha
    val g = color.green * (1 - alpha) + fogColor.green * alpha
    val b = color.blue * (1 - alpha) + fogColor.blue * alpha
    return Color(red = r, green = g, blue = b)
}
