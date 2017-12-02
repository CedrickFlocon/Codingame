package org.neige.codingame.hypersonic

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*


object BoardSpec : Spek({

    given("a board") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_game_one.txt"))
        val board = Board(scanner, 13, 11)

        on("first turn") {
            board.nextTurn()

            it("should add classic box in 10 3") {
                assertThat(board.getGridElement(Coordinate(10, 3))).isEqualTo(Box(10, 3))
            }

            it("should add a box with extra range in 12 5") {
                assertThat(board.getGridElement(Coordinate(12, 5))).isEqualTo(Box(12, 5))

            }

            it("should add a box with extra bomb in 4 2") {
                assertThat(board.getGridElement(Coordinate(4, 2))).isEqualTo(Box(4, 2))
            }

            it("should add a wall(1;1)") {
                assertThat(board.getGridElement(Coordinate(1, 1))).isEqualTo(Wall(1, 1))
            }

            it("should have player zero") {
                assertThat(board.getPlayer(0)).isEqualTo(Player(0, 0, 0, 1, 3))
            }

            it("should have player one") {
                assertThat(board.getPlayer(1)).isEqualTo(Player(1, 12, 10, 2, 5))
            }

            it("should have a player zero bomb") {
                assertThat(board.getGridElement(Coordinate(1, 0))).isEqualTo(Bomb(0, 1, 0, 5, 3))
            }

            it("should have a player one bomb") {
                assertThat(board.getGridElement(Coordinate(12, 9))).isEqualTo(Bomb(1, 12, 9, 3, 3))

            }

            it("should return the box 4 2 ") {
                assertThat(board.getClosestBox(Coordinate(5, 2))).isEqualTo(Box(4, 2))
            }
        }

        on("second turn") {
            board.nextTurn()

            it("should clear removed box 4 2") {
                assertThat(board.getGridElement(Coordinate(4, 2))).isEqualTo(Floor(4, 2))
            }

            it("should clear removed bomb") {
                assertThat(board.getGridElement(Coordinate(1, 0))).isEqualTo(Floor(1, 0))
            }

            it("should clear removed bomb") {
                assertThat(board.getGridElement(Coordinate(12, 9))).isEqualTo(Floor(12, 9))
            }
        }

        on("third turn") {
            board.nextTurn()

            it("should add a bomb on 4;2") {
                assertThat(board.getGridElement(Coordinate(4, 2))).isEqualTo(Bomb(0, 4, 2, 3, 3))
            }

            it("should exploded located 4;1") {
                assertThat(board.willExplode(Coordinate(4, 1))).isTrue()
            }

            it("should exploded located 4;2") {
                assertThat(board.willExplode(Coordinate(4, 2))).isTrue()
            }

            it("should exploded located 3;2") {
                assertThat(board.willExplode(Coordinate(3, 2))).isTrue()
            }

            it("should not exploded a box is between") {
                assertThat(board.willExplode(Coordinate(4, 0))).isFalse()
            }

            it("should not exploded out of range") {
                assertThat(board.willExplode(Coordinate(7, 2))).isFalse()
            }
        }
    }

})