package org.neige.codingame.puzzle

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import org.spekframework.spek2.style.specification.xdescribe
import java.util.*


object GameSpec : Spek({

    xdescribe("First game start") {
        val game = Game(Scanner(this::class.java.classLoader.getResourceAsStream("input_game_1.txt")))
        val answer = game.answer()

        xit("should be correct") {
            assertThat(answer).isEqualTo("answer")
        }
    }

    xdescribe("Second game start") {
        val game = Game(Scanner(this::class.java.classLoader.getResourceAsStream("input_game_2.txt")))
        val answer = game.answer()

        xit("should be correct") {
            assertThat(answer).isEqualTo("answer")
        }
    }

    xdescribe("third game start") {
        val game = Game(Scanner(this::class.java.classLoader.getResourceAsStream("input_game_3.txt")))
        val answer = game.answer()

        xit("should be correct") {
            assertThat(answer).isEqualTo("answer")
        }
    }

    xdescribe("fourth game start") {
        val game = Game(Scanner(this::class.java.classLoader.getResourceAsStream("input_game_4.txt")))
        val answer = game.answer()

        xit("should be correct") {
            assertThat(answer).isEqualTo("answer")
        }
    }

    xdescribe("fifth game start") {
        val game = Game(Scanner(this::class.java.classLoader.getResourceAsStream("input_game_5.txt")))
        val answer = game.answer()

        xit("should be correct") {
            assertThat(answer).isEqualTo("answer")
        }
    }

    xdescribe("sixth game start") {
        val game = Game(Scanner(this::class.java.classLoader.getResourceAsStream("input_game_6.txt")))
        val answer = game.answer()

        xit("should be correct") {
            assertThat(answer).isEqualTo("answer")
        }
    }

    xdescribe("seventh game start") {
        val game = Game(Scanner(this::class.java.classLoader.getResourceAsStream("input_game_7.txt")))
        val answer = game.answer()

        xit("should be correct") {
            assertThat(answer).isEqualTo("answer")
        }
    }

})