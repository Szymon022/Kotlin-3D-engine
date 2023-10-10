package obj

import data.Face
import data.Model
import data.Float3
import java.io.File

fun parseObj(path: String): Model {
    val vertices = mutableListOf<Float3>()
    val normals = mutableListOf<Float3>()
    val faces = mutableListOf<Face>()
    File(path).forEachLine { line ->
        when {
            line.startsWith("v") -> parseVec3(line, header = "v").also(vertices::add)
            line.startsWith("vn") -> parseVec3(line, header = "vn").also(vertices::add)
            line.startsWith("f") -> {
                line.substringAfter("f ")
                    .split(" ")
                    .also { iPairs ->
                        Face(
                            vertices = iPairs.map { vertices[it.substringBefore("//").toInt() - 1] }.toTypedArray(),
                            normals = iPairs.map { normals[it.substringAfter("//").toInt() - 1] }.toTypedArray()
                        ).also(faces::add)
                    }
            }
        }
    }
    return Model(faces.toTypedArray())
}

fun parseVec3(string: String, header: String, delimiter: String = " ") = string.substringAfter("$header$delimiter")
    .split(delimiter)
    .map(String::toFloat)
    .toVec3()

private fun List<Float>.toVec3() = Float3(
    x = this[0],
    y = this[1],
    z = this[2],
)
