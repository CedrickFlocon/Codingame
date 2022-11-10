package org.neige.codingame.puzzle

import com.google.common.truth.Truth.assertThat
import io.kotest.core.spec.style.DescribeSpec
import java.util.*


class GameSpec : DescribeSpec({

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