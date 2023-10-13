package math

import data.Float3
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.shouldBe

class VectorOperationsTest : FunSpec({

    isolationMode = IsolationMode.InstancePerTest

    context("Dot product") {

        test("Returns correct value for 2 non-orthogonal vectors") {
            val v = Float3(1f, 2f, 3f)
            val w = Float3(6f, 7f, 8f)
            (v o w).shouldBe(44f plusOrMinus 1e-7f)
        }

        test("Dot product is commutative") {
            val v = Float3(1f, 2f, 3f)
            val w = Float3(6f, 7f, 8f)
            val vw = v o w
            val wv = w o v
            vw.shouldBe(wv plusOrMinus 1e-7f)
        }

        test("Returns 0 for 2 orthogonal vectors") {
            val v = Float3(1f, 0f, 0f)
            val w = Float3(0f, 1f, 0f)
            (v o w).shouldBe(0f plusOrMinus 1e-7f)
        }

        test("Returns product of vectors lengths if vectors are parallel") {
            val v = Float3(3f, 0f, 0f)
            val w = Float3(6f, 0f, 0f)
            (v o w).shouldBe(18f plusOrMinus 1e-7f)
        }

        test("Returns negative number if vector have opposite direction") {
            val v = Float3(-3f, 0f, 0f)
            val w = Float3(6f, 0f, 0f)
            (v o w).shouldBe(-18f plusOrMinus 1e-7f)
        }
    }

    context("Cross product") {

        test("Returns 0 vector if vectors are parallel") {
            val v = Float3(-3f, 0f, 0f)
            val w = Float3(6f, 0f, 0f)
            val result = v x w
            result.x.shouldBe(0f plusOrMinus 1e-7f)
            result.y.shouldBe(0f plusOrMinus 1e-7f)
            result.z.shouldBe(0f plusOrMinus 1e-7f)
        }

        test("Returns correct vector for 2 non-parallel vectors") {
            val v = Float3(1f, 2f, 3f)
            val w = Float3(4f, 5f, 6f)
            val result = v x w
            result.x.shouldBe(-3f plusOrMinus 1e-7f)
            result.y.shouldBe(6f plusOrMinus 1e-7f)
            result.z.shouldBe(-3f plusOrMinus 1e-7f)
        }

        test("Operation is anti-commutative") {
            val v = Float3(1f, 2f, 3f)
            val w = Float3(4f, 5f, 6f)
            val res1 = v x w
            val res2 = w x v
            res1.x.shouldBe(-res2.x plusOrMinus 1e-7f)
            res1.y.shouldBe(-res2.y plusOrMinus 1e-7f)
            res1.z.shouldBe(-res2.z plusOrMinus 1e-7f)
        }

        test("Returned vector is perpendicular to input vectors") {
            val v = Float3(1f, 2f, 3f)
            val w = Float3(4f, 5f, 6f)
            val result = v x w
            (result o v).shouldBe(0f plusOrMinus 1e-7f)
            (result o w).shouldBe(0f plusOrMinus 1e-7f)
        }
    }
})