package org.neige.codingame.codersstrikeback

import org.assertj.core.api.Assertions.*
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object MoveSpec : Spek({

    given("A good move") {
        var move = Move(Coordinate(100.0, 200.0), "BOOST")

        on("on move") {
            it("Should print 100 200 BOOST") {
                assertThat(move.move()).isEqualTo("100 200 BOOST")
            }
        }
    }

    given("A wrong move") {
        var move = Move(Coordinate(100.0, 200.0))

        on("on move") {
            it("Should throw an IllegalStateException ") {
                assertThatThrownBy { move.move() }.isInstanceOf(IllegalStateException::class.java)
            }
        }

    }
})