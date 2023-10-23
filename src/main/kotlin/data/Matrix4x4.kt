package data

@JvmInline
value class Matrix4x4 private constructor(private val data: Array<FloatArray>) {

    constructor(
        row1: FloatArray,
        row2: FloatArray,
        row3: FloatArray,
        row4: FloatArray
    ) : this(arrayOf(row1, row2, row3, row4))

    operator fun get(i: Int, j: Int) = data[i][j]
}
