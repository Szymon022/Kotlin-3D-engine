package culling

import data.Face
import data.Float3
import math.minus
import math.o
import math.x

fun Face.isNotVisible(camera: Float3): Boolean {
    val (v1, v2, v3) = this.vertices
    val normal = (v2 - v1) x (v3 - v1)
    return normal o camera >= 0f
}
