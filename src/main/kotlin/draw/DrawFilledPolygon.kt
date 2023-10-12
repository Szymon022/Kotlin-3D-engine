package draw

import androidx.compose.ui.graphics.Canvas
import data.Face
import data.Float3

fun Canvas.drawFilledPolygon(face: Face) {
    val vertices = face.vertices
    val indexTable = vertices.createIndexTable()


}

fun Array<Float3>.createIndexTable() = mapIndexed { ordinal, v -> ordinal to v }
    .sortedBy { (_, v) -> v.y }
    .map { (index, _) -> index }
    .toIntArray()
