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

            it("should add a box 5 0") {
                assertThat(board.getGridElement(Coordinate(5, 0))).isEqualTo(Box(5, 0))
            }

            it("should have players zero") {
                assertThat(board.getPlayer(0)).isEqualTo(Player(0, 0, 0))
            }

            it("should have bomb from player zero") {
                assertThat(board.getGridElement(Coordinate(3, 5))).isEqualTo(Bomb(0, 3, 5))
            }

            it("should have players one") {
                assertThat(board.getPlayer(1)).isEqualTo(Player(1, 12, 10))
            }
        }

        given("located 1 0") {
            val coordinate = Coordinate(1, 0)

            on("find closeted box") {
                val box = board.getClosetedBox(coordinate)

                it("should return the box 1 2 ") {
                    assertThat(box).isEqualTo(Box(2, 1))
                }
            }
        }

        on("second turn") {
            board.nextTurn()

            it("should clear removed box") {
                assertThat(board.getGridElement(Coordinate(5, 0))).isEqualTo(Floor(5, 0))
            }

            it("should clear removed bomb") {
                assertThat(board.getGridElement(Coordinate(5, 0))).isEqualTo(Floor(5, 0))
            }
        }
    }

})