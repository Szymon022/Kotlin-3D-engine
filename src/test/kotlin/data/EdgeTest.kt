package data

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.floats.plusOrMinus
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe

class EdgeTest : FunSpec({

    isolationMode = IsolationMode.InstancePerTest

    context("Edge constructor") {
        test("slope is calculated correctly") {
            val x1 = 1f
            val x2 = 5f
            val y1 = 17f
            val y2 = 1f
            slope(x1 = x1, x2 = x2, y1 = y1, y2 = y2).shouldBe(-4f plusOrMinus 1e-7f)

        }


        test("Edge returns null when vertices create horizontal line") {
            val v1 = Float3(x = 2f, y = 2f, z = 3f)
            val v2 = Float3(x = 4f, y = 2f, z = 5f)
            val edge = Edge(v1, v2)
            edge.shouldBeNull()
        }

        test("Edge creates valid Edge data structure when vertices create vertical line") {
            val v1 = Float3(x = 1f, y = 2f, z = 3f)
            val v2 = Float3(x = 1f, y = 4f, z = 5f)
            val edge = Edge(v1, v2)
            edge.shouldNotBeNull()
            edge.yMin.shouldBe(2f)
            edge.yMax.shouldBe(4f)
            edge.x.shouldBe(1f)
            edge.overM.shouldBe(0f)
        }

        test("Edge creates valid Edge") {
            val v1 = Float3(x = 1f, y = 2f, z = 3f)
            val v2 = Float3(x = 5f, y = 10f, z = 6f)
            val edge = Edge(v1, v2)
            edge.shouldNotBeNull()
            edge.yMin.shouldBe(2f)
            edge.yMax.shouldBe(10f)
            edge.x.shouldBe(1f)
            edge.overM.shouldBe(0.5f plusOrMinus 1e-7f)
        }

        test("Edge is commutative") {
            val v1 = Float3(x = 1f, y = 2f, z = 3f)
            val v2 = Float3(x = 5f, y = 10f, z = 6f)
            val edge1 = Edge(v1, v2)
            val edge2 = Edge(v2, v1)
            edge1.shouldNotBeNull()
            edge2.shouldNotBeNull()
            edge1.yMin.shouldBe(edge2.yMin)
            edge1.yMax.shouldBe(edge2.yMax)
            edge1.x.shouldBe(edge2.x)
            edge1.overM.shouldBe(edge2.overM plusOrMinus 1e-7f)
        }
    }


})