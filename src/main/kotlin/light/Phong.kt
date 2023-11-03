package light

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import culling.ZBuffer
import data.*
import draw.moveToAET
import draw.updateEdges
import math.minus
import math.normalize
import math.plus
import math.times
import java.awt.image.BufferedImage

fun BufferedImage.drawTrianglePhong(
    face: CanvasFace,
    zBuffer: ZBuffer,
    kd: Float,
    ks: Float,
    m: Int,
    lightColor: Color,
    objColor: Color,
    light: Float3,
    camera: Camera,
) {
    val edgeTable = face.toEdgeTable().also { it.sortBy(Edge::yMin) }
    val activeEdgeTable = mutableListOf<Edge>()
    var scanline = edgeTable.first().yMin

    val vertices = face.vertices
    val (x1, y1, z1) = vertices[0]
    val (x2, y2, z2) = vertices[1]
    val (x3, y3, z3) = vertices[2]
    val divider = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3)

    val (v1Normal, v2Normal, v3Normal) = face.normals.map { it.normalize() }

    while (!(edgeTable.isEmpty() && activeEdgeTable.isEmpty())) {
        edgeTable.moveToAET(activeEdgeTable, scanline)

        val y = scanline.toInt()
        for (i in 0 until activeEdgeTable.size - 1 step 2) {
            val e1 = activeEdgeTable[i]
            val e2 = activeEdgeTable[i + 1]

            var x = e1.x.toInt()
            val xEnd = e2.x.toInt()
            while (x <= xEnd) {
                if (x >= 0 && x < zBuffer.width && y >= 0 && y < zBuffer.height) {
                    val commonXPart = x - x3
                    val commonYPart = scanline - y3
                    val w1 = ((y2 - y3) * commonXPart + (x3 - x2) * commonYPart) / divider
                    val w2 = ((y3 - y1) * commonXPart + (x1 - x3) * commonYPart) / divider
                    val w3 = 1 - w1 - w2

                    val z = w1 * z1 + w2 * z2 + w3 * z3
                    if (z < zBuffer[x, y]) {
                        zBuffer[x, y] = z
                        val interpolatedNormal = w1 * v1Normal + w2 * v2Normal + w3 * v3Normal
                        val observer = camera.position - Float3(x.toFloat(), y.toFloat(), z).normalize()
                        val color = lambert(kd, ks, m, lightColor, objColor, light, interpolatedNormal.normalize(), observer)

                        setRGB(x, y, color.toArgb())
                    }
                }
                x++
            }
        }

        scanline++
        activeEdgeTable.removeIf { it.yMax < scanline }
        activeEdgeTable.updateEdges()
    }
}
