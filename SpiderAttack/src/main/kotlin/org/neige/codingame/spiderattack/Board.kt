package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate

object Board {

    val BOARD_WIDTH = 17630
    val BOARD_HEIGHT = 9000
    val CENTER: Coordinate
        get() = Coordinate(BOARD_WIDTH / 2, BOARD_HEIGHT / 2)

    lateinit var myBase: Base
    lateinit var opponentBase: Base

    fun playerBase(player: Player): Base {
        return when (player) {
            Player.ME -> myBase
            Player.OPPONENT -> opponentBase
        }
    }

    fun opponentBase(player: Player): Base {
        return when (player) {
            Player.OPPONENT -> myBase
            Player.ME -> opponentBase
        }
    }

}
