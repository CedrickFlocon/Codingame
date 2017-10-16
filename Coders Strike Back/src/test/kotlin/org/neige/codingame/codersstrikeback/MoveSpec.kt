package org.neige.codingame.codersstrikeback

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object MoveSpec : Spek({

    given("a move") {
        val move = Move(Coordinate(100.0, 200.0), "100")

        on("on move") {
            it("Should print 100 200 100") {
                assertThat(move.toString()).isEqualTo("100 200 100")
            }
        }
    }

    given("a move with boost") {
        val move = Move(Coordinate(100.0, 200.0), "BOOST")

        on("on move") {
            it("Should print 100 200 BOOST") {
                assertThat(move.toString()).isEqualTo("100 200 BOOST")
            }
        }
    }

    given("a move with shield") {
        val move = Move(Coordinate(100.0, 200.0), "SHIELD")

        on("on move") {
            it("Should print 100 200 SHIELD") {
                assertThat(move.toString()).isEqualTo("100 200 SHIELD")
            }
        }
    }

})