package asserts

import data.Face

fun Face.shouldBe(expected: Face): Face {
    vertices.zip(expected.vertices).forEach { (expected, actual) ->
        actual.shouldBe(expected)
    }
    normals.zip(expected.normals).forEach { (expected, actual) ->
        actual.shouldBe(expected)
    }
    return this
}