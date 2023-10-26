package data

data class Face(
    val vertices: Array<Float4>,
    val normals: Array<Float3>,
)

data class CanvasFace(
    val vertices: Array<Float3>,
    val normals: Array<Float3>,
)
