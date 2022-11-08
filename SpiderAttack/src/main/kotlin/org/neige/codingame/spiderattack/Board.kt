package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate

object Board {

    val BOARD_WIDTH = 17630
    val BOARD_HEIGHT = 9000
    val CENTER: Coordinate
        get() = Coordinate(BOARD_WIDTH / 2, BOARD_HEIGHT / 2)

    private lateinit var myBase: Base
    private lateinit var opponentBase: Base

    fun init(myBase: Base) {
        this.myBase = myBase
        opponentBase = Base(
            if (Board.myBase.coordinate.x == 0) Coordinate(17630, 9000) else Coordinate(0, 0),
            Player.OPPONENT
        )
    }

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
