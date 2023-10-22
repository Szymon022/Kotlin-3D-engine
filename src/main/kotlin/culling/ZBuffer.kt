package culling

import kotlinx.coroutines.sync.Mutex

class ZBuffer(val width: Int, val height: Int) {

    val mutex = Mutex()

    private val buffer = FloatArray(width * height) { Float.MAX_VALUE }

    operator fun get(x: Int, y: Int) = buffer[x * width + y]

    operator fun set(x: Int, y: Int, value: Float) {
        buffer[x * width + y] = value
    }
}
