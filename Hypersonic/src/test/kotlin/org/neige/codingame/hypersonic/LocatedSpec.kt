package org.neige.codingame.hypersonic

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


object LocatedSpec : Spek({

    given("a located") {
        val located = Coordinate(2, 3)

        given("an other located") {
            val other = Box(1, 5)

            on("distance between") {
                val distanceBetween = located.distanceBetween(other)

                it("should be equals to 3") {
                    assertThat(distanceBetween).isEqualTo(3)
                }
            }
        }
    }

    given("a located 5 5") {
        val coordinate = Coordinate(5, 5)

        on("check if is neighbour with 6 5") {
            val checkNeighbour = coordinate.checkNeighbour(Coordinate(6, 5))

            it("should be a neighbour") {
                assertThat(checkNeighbour).isTrue()
            }
        }

        on("check if is neighbour with 5 6") {
            val checkNeighbour = coordinate.checkNeighbour(Coordinate(5, 6))

            it("should be a neighbour") {
                assertThat(checkNeighbour).isTrue()
            }
        }

        on("check is neighbour with 6 6") {
            val checkNeighbour = coordinate.checkNeighbour(Coordinate(6, 6))

            it("should not be a neighbour") {
                assertThat(checkNeighbour).isFalse()
            }
        }
    }

})

data class Coordinate(override val x: Int, override val y: Int) : Located