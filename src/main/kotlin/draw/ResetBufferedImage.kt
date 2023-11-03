package draw

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import java.awt.image.BufferedImage

fun BufferedImage.reset(color: Color) {
    createGraphics().apply {
        this.color = java.awt.Color(color.toArgb())
        fillRect(0, 0, width, height)
    }
}