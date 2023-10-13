package draw

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Paint
import data.Edge
import data.Face
import data.toEdgeTable
import java.awt.image.BufferedImage

fun Canvas.drawFilledPolygon(face: Face, paint: Paint) {
    val edgeTable = face.toEdgeTable().also { it.sortBy(Edge::yMin) }
    val activeEdgeTable = mutableListOf<Edge>()
    var scanline = edgeTable.first().yMin

    while (!(edgeTable.isEmpty() && activeEdgeTable.isEmpty())) {
        edgeTable.moveToAET(activeEdgeTable, scanline)
        drawLine(activeEdgeTable, scanline, paint)
        scanline++
        activeEdgeTable.removeIf { it.yMax < scanline }
        activeEdgeTable.updateEdges()
    }
}

fun BufferedImage.drawFilledPolygon(face: Face, color: Int) {
    val edgeTable = face.toEdgeTable().also { it.sortBy(Edge::yMin) }
    val activeEdgeTable = mutableListOf<Edge>()
    var scanline = edgeTable.first().yMin

    while (!(edgeTable.isEmpty() && activeEdgeTable.isEmpty())) {
        edgeTable.moveToAET(activeEdgeTable, scanline)
        drawLine(activeEdgeTable, scanline, color)
        scanline++
        activeEdgeTable.removeIf { it.yMax < scanline }
        activeEdgeTable.updateEdges()
    }
}

fun MutableList<Edge>.moveToAET(aet: MutableList<Edge>, scanline: Float) {
    val toRemove = mutableListOf<Edge>()
    for (edge in this) {
        if (edge.yMin < scanline) {
            aet.add(edge)
            toRemove.add(edge)
        } else {
            break
        }
    }
    toRemove.forEach(this::remove)
    aet.sortBy(Edge::x)
}

fun Canvas.drawLine(aet: MutableList<Edge>, scanline: Float, paint: Paint) {
    for (i in 0 until aet.size - 1 step 2) {
        val e1 = aet[i]
        val e2 = aet[i + 1]
        drawLine(
            p1 = Offset(x = e1.x, y = scanline),
            p2 = Offset(x = e2.x, y = scanline),
            paint = paint
        )
    }
}

fun BufferedImage.drawLine(aet: MutableList<Edge>, scanline: Float, color: Int) {
    for (i in 0 until aet.size - 1 step 2) {
        val e1 = aet[i]
        val e2 = aet[i + 1]
        drawLine(x1 = e1.x, x2 = e2.x, y = scanline, color = color)
    }
}

fun BufferedImage.drawLine(x1: Float, x2: Float, y: Float, color: Int) {
    var x = x1.toInt()
    val y = y.toInt()
    val x2 = x2.toInt()
    while (x <= x2) {
        setRGB(x, y, color)
        x++
    }
}

fun MutableList<Edge>.updateEdges() = forEach {
    it.x += it.overM
}

