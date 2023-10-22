package culling

class ZBuffer private constructor(
    private val buffer: FloatArray,
    private val width: Int,
    private val height: Int,
) {

    constructor(width: Int, height: Int) : this(
        buffer = FloatArray(width * height) { Float.MAX_VALUE },
        width = width,
        height = height
    )
}

