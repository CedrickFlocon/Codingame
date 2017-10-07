package org.neige.codingame.codersstrikeback

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object CoordinateSpec : Spek({

    given("first coordinate") {
        val origin = Coordinate(100.0, 100.0)

        on("calculate distance from other coordinate") {
            val arrival = Coordinate(200.0, 200.0)
            it("should be 141 distance from origin") {
                assertThat(Math.round(origin.distanceFrom(arrival))).isEqualTo(141)
            }
        }

        given("a vector 50;50") {
            val v = Vector(50.0, 50.0)

            on("addition to the vector") {
                val coordinate = origin - v

                it("should be equals to 50;50") {
                    assertThat(coordinate).isEqualTo(Coordinate(50.0, 50.0))
                }
            }
        }

        given("a vector -50;50") {
            val v = Vector(-50.0, 50.0)

            on("addition to the vector") {
                val coordinate = origin - v

                it("should be equals to 150;50") {
                    assertThat(coordinate).isEqualTo(Coordinate(150.0, 50.0))
                }
            }
        }

        given("3 collinear point") {
            val opponentPosition = Coordinate(100.0, 200.0)
            val checkpointPosition = Coordinate(100.0, 300.0)
            on("check collinear") {
                val isCollinear = origin.collinear(opponentPosition, checkpointPosition)
                it("should be collinear") {
                    assertThat(isCollinear).isTrue()
                }
            }
        }

        given("3 not collinear point") {
            val opponentPosition = Coordinate(100.0, 200.0)
            val checkpointPosition = Coordinate(200.0, 300.0)
            on("check collinear") {
                val isCollinear = origin.collinear(opponentPosition, checkpointPosition)
                it("should not be collinear") {
                    assertThat(isCollinear).isFalse()
                }
            }
        }

    }

})