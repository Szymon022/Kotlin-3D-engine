package obj

import data.Face
import data.Float3
import data.Float4
import data.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

suspend fun parseObj(path: String): Model = withContext(Dispatchers.IO) {
    val vertices = mutableListOf<Float4>()
    val normals = mutableListOf<Float3>()
    val faces = mutableListOf<Face>()
    File(path).forEachLine { line ->
        when {
            line.startsWith("v ") -> parseFloat4(line, header = "v").also(vertices::add)
            line.startsWith("vn ") -> parseFloat3(line, header = "vn").also(normals::add)
            line.startsWith("f ") -> {
                line.substringAfter("f ")
                    .split(" ")
                    .also { iPairs ->
                        Face(
                            vertices = iPairs.map { vertices[it.substringBefore("/").toInt() - 1] }.toTypedArray(),
                            normals = iPairs.map { normals[it.substringAfterLast("/").toInt() - 1] }.toTypedArray()
                        ).also(faces::add)
                    }
            }
        }
    }
    return@withContext Model(faces.toTypedArray())
}

fun parseFloat4(string: String, header: String, delimiter: String = " ") = parseFloat3(string, header, delimiter).let {
    Float4(it.x, it.y, it.z, 1f)
}

fun parseFloat3(string: String, header: String, delimiter: String = " ") = string.substringAfter("$header$delimiter")
    .split(delimiter)
    .map(String::toFloat)
    .toFloat3()

private fun List<Float>.toFloat3() = Float3(
    x = this[0],
    y = this[1],
    z = this[2],
)
