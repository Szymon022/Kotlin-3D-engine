package obj

import asserts.shouldBe
import data.Face
import data.Float3
import data.Model
import io.kotest.core.spec.style.FunSpec

class ObjParserTest : FunSpec({

    val testModelPath = "D:\\IdeaProjects\\Engine3D\\src\\test\\kotlin\\files\\TestObjModel.obj"

    test("parseFloat3 correctly parses Float3 from line with given header and delimiter") {
        val vertexLine = "v 0.000000 -0.555570 0.636835"
        val normalLine = "vn -0.0846 0.2790 -0.9565"
        val parsedVertex = parseFloat3(vertexLine, header = "v", delimiter = " ")
        parsedVertex.shouldBe(Float3(0.0f, -0.55557f, 0.636835f))
        val parsedNormal = parseFloat3(normalLine, header = "vn", delimiter = " ")
        parsedNormal.shouldBe(Float3(-0.0846f, 0.279f, -0.9565f))
    }

    test("parseObj should correctly parse Model from .obj file") {
        val model = parseObj(testModelPath)
        val expectedModel = Model(
            faces = arrayOf(
                Face(
                    vertices = arrayOf(
                        Float3(0f, -0.55557f, 0.636835f),
                        Float3(-0.038060f, 0.191342f, 0.786151f),
                        Float3(0.000000f, -0.980785f, -0.000000f)
                    ),
                    normals = arrayOf(
                        Float3(-0.0097f, -0.0980f, -0.9951f),
                        Float3(-0.0939f, 0.9529f, -0.2884f),
                        Float3(-0.0097f, 0.0980f, -0.9951f)
                    )
                ),
                Face(
                    vertices = arrayOf(
                        Float3(0.000000f, -0.831470f, 0.360935f),
                        Float3(-0.074658f, 0.375330f, 0.729246f),
                        Float3(-0.392847f, -0.587938f, 0.512472f)
                    ),
                    normals = arrayOf(
                        Float3(0.8786f, -0.0865f, -0.4696f),
                        Float3(0.1847f, 0.6088f, -0.7715f),
                        Float3(-0.0846f, 0.2790f, -0.9565f)
                    )
                )
            )
        )
        model.faces.zip(expectedModel.faces).forEach { (actual, expected) ->
            actual.shouldBe(expected)
        }
    }
})
