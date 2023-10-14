package data

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.shouldBe
import kotlin.math.sqrt

class Float3Test : FunSpec({

    context("Length") {

        test("Returns correct length if 0 vector is given") {
            val v = Float3(0f, 0f, 0f)
            v.length.shouldBe(0f plusOrMinus 1e-7f)
        }

        test("Returns correct length for 3d vector") {
            val v = Float3(1f, 1f, 1f)
            v.length.shouldBe(sqrt(3f) plusOrMinus 1e-7f)
        }

        test("Returns correct length") {
            val v = Float3(3f, 4f, 5f)
            v.length.shouldBe(sqrt(50f) plusOrMinus 1e-7f)
        }
    }
})