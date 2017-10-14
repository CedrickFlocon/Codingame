package org.neige.codingame.puzzle

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object LocationSpec : Spek({

    given("a location") {
        val location = Location(0.0, 0.0)
        on("calculate distance from the same location") {
            val distanceFrom = location.distanceFrom(location)
            it("should be equals to 0") {
                assertThat(distanceFrom).isZero()
            }
        }

        on("calculate distance from an other location") {
            val distanceFrom = location.distanceFrom(Location(1.0, 1.0))
            it("should be equals to 157") {
                assertThat(distanceFrom).isEqualTo(157.2503794930137)
            }
        }
    }


})