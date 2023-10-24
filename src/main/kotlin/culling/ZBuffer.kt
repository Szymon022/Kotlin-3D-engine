package culling

class ZBuffer(val width: Int, val height: Int) {

    private val buffer = FloatArray(width * height) { Float.MAX_VALUE }

    operator fun get(x: Int, y: Int) = buffer[x + y * width]

    operator fun set(x: Int, y: Int, value: Float) {
        buffer[x + y * width] = value
    }
}
