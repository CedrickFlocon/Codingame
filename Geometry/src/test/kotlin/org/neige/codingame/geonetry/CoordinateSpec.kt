package org.neige.codingame.geonetry

import org.assertj.core.api.Assertions.assertThat
import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object CoordinateSpec : Spek({

    describe("a coordinate") {
        val coordinate by memoized { Coordinate(1.0, 1.0) }

        describe("a vector") {
            val vector by memoized { Vector(10.0, 5.0) }

            it("plus") {
                assertThat(coordinate + vector).isEqualTo(Coordinate(11.0, 6.0))
            }
        }

        describe("a coordinate") {
            val secondCoordinate by memoized { Coordinate(1.0, 10.0) }

            it("distance") {
                assertThat(coordinate.distanceFrom(secondCoordinate)).isEqualTo(9.0)
            }
        }
    }
})
