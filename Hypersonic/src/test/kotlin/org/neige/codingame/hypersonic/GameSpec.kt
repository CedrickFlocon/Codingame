package org.neige.codingame.hypersonic

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


object GameSpec : Spek({

    given("a game") {
        val board = mock(Board::class.java)
        val playerId = 0
        val game = Game(board, playerId)

        given("an action") {
            val action = mock(Action::class.java)
            val player = mock(Player::class.java)
            given(board.getPlayer(playerId)).willReturn(player)
            given(player.play(board)).willReturn(action)

            on("next turn") {
                game.nextTurn()

                it("should notify the board") {
                    verify(board).nextTurn()
                }

                it("should notify the player to play") {
                    verify(player).play(board)
                }

                it("should play the action") {
                    verify(action).play()
                }

            }
        }
    }

})