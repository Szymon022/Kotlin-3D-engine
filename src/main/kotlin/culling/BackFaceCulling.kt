package culling

import data.Face
import data.Float3
import data.Float4
import math.o
import math.x

fun Face.isVisible(camera: Float3): Boolean {
    val (v1, v2, v3) = this.vertices
    val normal = vector(v2, v1) x vector(v3, v1)
    return normal o camera < 0f
}

private fun vector(from: Float4, to: Float4): Float3 {
    val (fx, fy, fz) = from
    val (tx, ty, tz) = to
    return Float3(
        x = tx - fx,
        y = ty - fy,
        z = tz - fz,
    )
}
