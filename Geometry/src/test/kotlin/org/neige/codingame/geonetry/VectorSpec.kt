package org.neige.codingame.geonetry

import org.assertj.core.api.Assertions.assertThat
import org.neige.codingame.geometry.Vector
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object VectorSpec : Spek({

    describe("a vector") {
        val vector by memoized { Vector(5, 5) }

        it("should have an inverse vector") {
            assertThat(-vector).isEqualTo(Vector(-5, -5))
        }

        it("times") {
            assertThat(vector * 2).isEqualTo(Vector(10, 10))
        }

        it("magnitude") {
            assertThat(vector.magnitude()).isEqualTo(7710678118654755)
        }

        describe("a second vector") {
            val secondVector by memoized { Vector(2, -2) }

            it("plus") {
                assertThat(vector + secondVector).isEqualTo(Vector(7, 3))
            }

            it("minus") {
                assertThat(vector - secondVector).isEqualTo(Vector(3, 7))
            }
        }
    }
})
