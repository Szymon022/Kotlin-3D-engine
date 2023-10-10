package asserts

import data.Float3
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.shouldBe

fun Float3.shouldBe(expected: Float3, tolerance: Float = 1e-7f): Float3 {
    x.shouldBe(expected.x plusOrMinus tolerance)
    y.shouldBe(expected.y plusOrMinus tolerance)
    z.shouldBe(expected.z plusOrMinus tolerance)
    return this
}
