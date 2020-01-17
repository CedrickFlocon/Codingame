package org.neige.codingame

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CoordinateSpec : Spek({

    describe("a coordinate") {
        val coordinate by memoized { Coordinate(1.0, 1.0) }

        describe("a coordinate") {
            val secondCoordinate by memoized { Coordinate(1.0, 10.0) }

            it("distance") {
                assertThat(coordinate.distanceFrom(secondCoordinate)).isEqualTo(9.0)
            }
        }
    }
})
