package org.neige.codingame

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object VectorSpec : Spek({

    describe("a vector") {
        val vector by memoized { Vector(5.0, 5.0) }

        it("should have an inverse vector") {
            assertThat(-vector).isEqualTo(Vector(-5.0, -5.0))
        }

        it("times") {
            assertThat(vector * 2.0).isEqualTo(Vector(10.0, 10.0))
        }

        it("magnitude") {
            assertThat(vector.magnitude()).isEqualTo(7.0710678118654755)
        }

        describe("a second vector") {
            val secondVector by memoized { Vector(2.0, -2.0) }

            it("plus") {
                assertThat(vector + secondVector).isEqualTo(Vector(7.0, 3.0))
            }

            it("minus") {
                assertThat(vector - secondVector).isEqualTo(Vector(3.0, 7.0))
            }
        }
    }
})
