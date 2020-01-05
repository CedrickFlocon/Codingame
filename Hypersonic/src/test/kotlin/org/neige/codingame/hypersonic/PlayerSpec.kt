package org.neige.codingame.hypersonic

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.jetbrains.spek.api.dsl.xgiven
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock


object PlayerSpec : Spek({

    xgiven("a player") {
        val player = Player(0, 12, 5, 1, 3)

        given("a board with a box") {
            val box = mock(Box::class.java)
            val board = mock(Board::class.java)
            val x = 5
            val y = 6
            given(box.x).willReturn(x)
            given(box.y).willReturn(y)
            given(board.getClosestBox(player)).willReturn(box)

            given("this box as a neighbour") {
                beforeGroup {
                    given(box.checkNeighbour(player)).willReturn(true)
                }

                on("play") {
                    val action = player.play(board)

                    it("should return a bomb 5 6 action") {
                        assertThat(action).isEqualTo(Action(Action.Command.BOMB, Coordinate(x, y)))
                    }
                }
            }

            given("this box as not a neighbour") {
                beforeGroup {
                    given(box.checkNeighbour(player)).willReturn(false)
                }

                on("play") {
                    val action = player.play(board)

                    it("should return a bomb 5 6 action") {
                        assertThat(action).isEqualTo(Action(Action.Command.MOVE, Coordinate(x, y)))
                    }
                }
            }
        }

        given("a board without a box") {
            val board = mock(Board::class.java)
            given(board.getClosestBox(player)).willReturn(null)

            on("play") {
                val action = player.play(board)

                it("should move to 0 0") {
                    assertThat(action).isEqualTo(Action(Action.Command.MOVE, Coordinate(0, 0)))
                }
            }
        }
    }

})