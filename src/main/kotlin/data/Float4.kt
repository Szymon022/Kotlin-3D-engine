package data

data class Float4(val x: Float, val y: Float, val z: Float, val w: Float) {

    fun normalize() = Float3(x / w, y / w, z / w)
}
