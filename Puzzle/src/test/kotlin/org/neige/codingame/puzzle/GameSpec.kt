package org.neige.codingame.puzzle

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*


object GameSpec : Spek({

    given("First game start") {
        val game = Game(Scanner(this::class.java.classLoader.getResourceAsStream("input_game_1")))
        game.nextStep()

        on("answer") {
            val answer = game.answer()
            it("should be correct") {
                assertThat(answer).isEqualTo("answer")
            }
        }

    }

    given("Second game start") {
        val game = Game(Scanner(this::class.java.classLoader.getResourceAsStream("input_game_2")))
        game.nextStep()

        on("answer") {
            val answer = game.answer()
            it("should be correct") {
                assertThat(answer).isEqualTo("answer")
            }
        }

    }

    given("third game start") {
        val game = Game(Scanner(this::class.java.classLoader.getResourceAsStream("input_game_3")))
        game.nextStep()

        on("answer") {
            val answer = game.answer()
            it("should be correct") {
                assertThat(answer).isEqualTo("answer")
            }
        }

    }

})