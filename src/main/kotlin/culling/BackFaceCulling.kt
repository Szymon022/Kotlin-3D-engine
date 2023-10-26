package culling

import data.CanvasFace
import data.Float3
import math.o
import math.x

fun CanvasFace.isVisible(camera: Float3): Boolean {
    val (v1, v2, v3) = this.vertices
    val normal = vector(v2, v1) x vector(v3, v1)
    return normal o camera < 0f
}

private fun vector(from: Float3, to: Float3): Float3 {
    val (fx, fy, fz) = from
    val (tx, ty, tz) = to
    return Float3(
        x = tx - fx,
        y = ty - fy,
        z = tz - fz,
    )
}
