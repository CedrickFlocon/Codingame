package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate
import kotlin.math.roundToInt


class Pac(
        val id: Int,
        val team: Team,
        val type: Type,
        val speedTurnsLeft: Int,
        val abilityCooldown: Int,
        val coordinate: Coordinate
) {

    fun action(board: Board): Command? {
        val maxPellet = listOf(board.straightLinePellet(this.coordinate, Board.Direction.LEFT),
                board.straightLinePellet(this.coordinate, Board.Direction.UP),
                board.straightLinePellet(this.coordinate, Board.Direction.RIGHT),
                board.straightLinePellet(this.coordinate, Board.Direction.DOWN))
                .maxBy { it.sumBy { ((it.value.toDouble() * 1 / it.coordinate.distanceFrom(this.coordinate)) * 100).roundToInt() } }!!

        return if (maxPellet.size > 2 && abilityCooldown == 0) {
            speed()
        } else if (maxPellet.isNotEmpty()) {
            move((if (maxPellet.size > 2) maxPellet[1] else maxPellet.first()).coordinate)
        } else {
            return move(board.closestPellet(coordinate).coordinate)
        }
    }

    private fun move(coordinate: Coordinate) = Move(id, coordinate, "$coordinate")

    private fun switch(type: Type): Switch {
        assert(this.type != type)
        return Switch(id, type)
    }

    private fun speed(): Speed {
        assert(abilityCooldown == 0)
        return Speed(id)
    }

    enum class Team {
        ALLY,
        ENEMY
    }

    enum class Type {
        ROCK,
        PAPER,
        SCISSORS
    }

}
