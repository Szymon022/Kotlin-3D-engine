package light

import androidx.compose.ui.graphics.Color
import data.Float3
import math.minus
import math.o
import math.times
import kotlin.math.pow

/**
 * kd - diffused
 * ks - specular
 * m - reflection,
 * lightColor - [r, g, b], r,g,b in [0;1]
 * objColor - [r, g, b], r,g,b in [0;1]
 * normal, observer - normalized
 * @return Color
 */
fun lambert(
    kd: Float,
    ks: Float,
    m: Int,
    lightColor: Color,
    objColor: Color,
    light: Float3,
    normal: Float3,
    observer: Float3
): Color {
    val (rLight, gLight, bLight) = lightColor
    val (rObj, gObj, bObj) = objColor
    val reflection = 2 * (normal o light).coerceAtLeast(0f) * normal - light
    val commonPart = (kd * (normal o light).coerceAtLeast(0f) +
            ks * (observer o reflection).pow(m)).coerceIn(0f..Float.MAX_VALUE)
    return Color(
        // probably there will be coerceAtMost(1f) needed
        red = (rLight * rObj * commonPart).coerceIn(0f, 1f),
        blue = (gLight * gObj * commonPart).coerceIn(0f, 1f),
        green = (bLight * bObj * commonPart).coerceIn(0f, 1f),
    )
}
