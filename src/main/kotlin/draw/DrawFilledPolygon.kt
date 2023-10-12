package draw

import androidx.compose.ui.graphics.Canvas
import data.Edge
import data.Face
import data.toEdgeTable

fun Canvas.drawFilledPolygon(face: Face) {
    val edgeTable = face.toEdgeTable().sortBy(Edge::yMin)
    val activeEdgeTable = mutableListOf<Edge>()

}

