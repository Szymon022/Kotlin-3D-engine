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
                val rgb = getRGB(x, y)
                setRGB(x, y, blend(rgb, fog.color.toArgb(), alpha))
            }
        }
    }
}

private fun blend(color: Int, fogColor: Int, alpha: Float): Int {
    val r = (((color and 0xFF0000) shr 16) * (1 - alpha) + ((fogColor and 0xFF0000) shr 16) * alpha).toInt() shl 16
    val g = (((color and 0xFF00) shr 8) * (1 - alpha) + ((fogColor and 0xFF00) shr 8) * alpha).toInt() shl 8
    val b = ((color and 0xFF) * (1 - alpha) + (fogColor and 0xFF) * alpha).toInt()
    return (-16777216 or r or g or b)
}
