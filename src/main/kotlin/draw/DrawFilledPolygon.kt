package draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import data.Edge
import data.Face
import data.toEdgeTable

fun Canvas.drawFilledPolygon(face: Face, paint: Paint) {
    val edgeTable = face.toEdgeTable().also { it.sortBy(Edge::yMin) }
    val activeEdgeTable = mutableListOf<Edge>()
    var scanline = edgeTable.first().yMin

    while (!(edgeTable.isEmpty() && activeEdgeTable.isEmpty())) {
        edgeTable.moveToAET(activeEdgeTable, scanline)
        drawLine(activeEdgeTable, scanline, paint)
        activeEdgeTable.removeIf { it.yMax <= scanline }
        scanline++
        activeEdgeTable.updateEdges()
    }
}

fun MutableList<Edge>.moveToAET(aet: MutableList<Edge>, scanline: Float) {
    for (edge in this) {
        if (edge.yMin <= scanline) {
            aet.add(edge)
            remove(edge)
        } else {
            break
        }
    }
    aet.sortBy(Edge::x)
}

fun Canvas.drawLine(aet: MutableList<Edge>, scanline: Float, paint: Paint) {
    for (i in 0 until aet.size step 2) {
        val e1 = aet[i]
        val e2 = aet[i + 1]
        drawLine(
            p1 = Offset(x = e1.x, y = scanline),
            p2 = Offset(x = e2.x, y = scanline),
            paint = paint
        )
    }
}

fun MutableList<Edge>.updateEdges() = forEach {
    it.x += it.overM
}

