package light

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import data.Edge
import data.Face
import data.Float3
import data.toEdgeTable
import draw.moveToAET
import draw.updateEdges
import math.normalize
import math.plus
import math.times
import java.awt.image.BufferedImage

fun BufferedImage.drawStillTriangle(
    face: Face,
    kd: Float,
    ks: Float,
    m: Int,
    lightColor: Color,
    objColor: Color,
    light: Float3,
    observer: Float3,
) {
    val edgeTable = face.toEdgeTable().also { it.sortBy(Edge::yMin) }
    val activeEdgeTable = mutableListOf<Edge>()
    var scanline = edgeTable.first().yMin

    val (n1, n2, n3) = face.normals
    val approxNormal = (0.33333334f * (n1 + n2 + n3)).normalize()
    val fillColor = lambert(kd, ks, m, lightColor, objColor, light, approxNormal, observer).toArgb()

    while (!(edgeTable.isEmpty() && activeEdgeTable.isEmpty())) {
        edgeTable.moveToAET(activeEdgeTable, scanline)

        val y = scanline.toInt()
        for (i in 0 until activeEdgeTable.size - 1 step 2) {
            val e1 = activeEdgeTable[i]
            val e2 = activeEdgeTable[i + 1]

            var x = e1.x.toInt()
            val xEnd = e2.x.toInt()
            while (x <= xEnd) {
                setRGB(x, y, fillColor)
                x++
            }
        }

        scanline++
        activeEdgeTable.removeIf { it.yMax < scanline }
        activeEdgeTable.updateEdges()
    }

}