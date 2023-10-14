package light

import androidx.compose.ui.graphics.Color
import data.Float3
import math.o
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
    lightColor: Float3,
    objColor: Float3,
    light: Float3,
    normal: Float3,
    observer: Float3
): Color {
    val (rLight, gLight, bLight) = lightColor
    val (rObj, gObj, bObj) = objColor
    val commonPart = kd * (normal o light).coerceAtLeast(0f) +
            ks * (observer o normal).coerceAtLeast(0f).pow(m)
    return Color(
        // probably there will be coerceAtMost(1f) needed
        red = rLight * rObj * commonPart,
        blue = gLight * gObj * commonPart,
        green = bLight * bObj * commonPart,
    )
}
