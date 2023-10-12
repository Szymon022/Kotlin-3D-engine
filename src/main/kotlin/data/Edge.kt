package data

import kotlin.math.abs

data class Edge(
    val yMin: Float,
    val yMax: Float,
    var x: Float,
    val overM: Float,
)

fun Face.toEdgeTable(): MutableList<Edge> {
    val size = vertices.size
    val edgeTable = mutableListOf<Edge>()
    repeat(size) { i ->
        val (x1, y1) = vertices[i % size]
        val (x2, y2) = vertices[(i + 1) % size]
        when {
            abs(x1 - x2) <= 1e-7f -> { // 1/m is just 0 --> x will not change
                if (y1 < y2) {
                    Edge(yMin = y1, yMax = y2, x = x1, overM = 0f)
                } else {
                    Edge(yMin = y2, yMax = y1, x = x2, overM = 0f)
                }
            }

            abs(y1 - y2) <= 1e-7f -> null // -- 1/m is 1/0 --> ignore this edge
            y1 > y2 -> Edge(yMin = y2, yMax = y1, x = x2, overM = 1 / slope(x1 = x1, x2 = x2, y1 = y1, y2 = y2))
            y1 < y2 -> Edge(yMin = y1, yMax = y2, x = x1, overM = 1 / slope(x1 = x1, x2 = x2, y1 = y1, y2 = y2))
            else -> null
        }?.let { edgeTable.add(it) }
    }
    return edgeTable
}

fun slope(x1: Float, x2: Float, y1: Float, y2: Float) = (y2 - y1) / (x2 - x1)
