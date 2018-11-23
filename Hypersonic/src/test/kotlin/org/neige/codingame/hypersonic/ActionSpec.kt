package org.neige.codingame.hypersonic

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.io.ByteArrayOutputStream
import java.io.PrintStream


object ActionSpec : Spek({

    given("a move action") {
        val x = 2
        val y = 3
        val action = Action(Action.Command.MOVE, Coordinate(x, y))

        on("play") {
            val out = System.out
            val outContent = ByteArrayOutputStream()
            System.setOut(PrintStream(outContent))
            action.play()
            System.setOut(out)

            it("should print \"MOVE 2 3\"") {
                assertThat(outContent.toString()).isEqualTo("MOVE $x $y\n")
            }
        }
    }

    given("a bomb action") {
        val x = 7
        val y = 14
        val action = Action(Action.Command.BOMB, Coordinate(x, y))

        on("play") {
            val out = System.out
            val outContent = ByteArrayOutputStream()
            System.setOut(PrintStream(outContent))
            action.play()
            System.setOut(out)

            it("should print \"BOMB 7 14\"") {
                assertThat(outContent.toString()).isEqualTo("BOMB $x $y\n")
            }
        }
    }

})