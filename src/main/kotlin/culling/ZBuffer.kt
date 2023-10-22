package culling

class ZBuffer private constructor(
    private val buffer: FloatArray,
    val width: Int,
    val height: Int,
) {

    constructor(width: Int, height: Int) : this(
        buffer = FloatArray(width * height) { Float.MAX_VALUE },
        width = width,
        height = height
    )

    operator fun get(x: Int, y: Int) = buffer[x * width + y]

    operator fun set(x: Int, y: Int, value: Float) {
        buffer[x * width + y] = value
    }
}
