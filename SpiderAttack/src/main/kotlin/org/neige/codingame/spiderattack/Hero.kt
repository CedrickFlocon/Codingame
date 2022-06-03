package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector

class Hero(
    val id: Int,
    val coordinate: Coordinate,
    val owner: Player,
) {

    companion object {
        const val VISIBILITY = 2200
    }

    val role: Role
        get() = if (otherHero.all { it.id < id }) {
            Role.DEFENDER
        } else if (otherHero.all { it.id > id }) {
            Role.ATTACKER
        } else {
            Role.MIDDLE
        }

    val bestPosition: Coordinate
        get() = when (role) {
            Role.DEFENDER -> Board.playerBase(owner).coordinate.let {
                val v = Vector(2500, 2500 * Board.BOARD_WIDTH / Board.BOARD_HEIGHT)
                if (it.x == 0) it + v else it - v
            }
            Role.MIDDLE -> Board.playerBase(owner).coordinate.let {
                val v = Vector(2500 * Board.BOARD_WIDTH / Board.BOARD_HEIGHT, 2500)
                if (it.x == 0) it + v else it - v
            }
            Role.ATTACKER -> Board.playerBase(owner).coordinate.let {
                val v = Vector(2500, 2500)
                if (it.x == 0) it + v else it - v
            }
        }

    var otherHero = mutableListOf<Hero>()
    var opponentsHero = mutableListOf<Hero>()
    var spiders = mutableListOf<Spider>()

    fun action(): Action {
        debug()

        return when (role) {
            Role.DEFENDER -> defend()
            Role.MIDDLE -> defend()
            Role.ATTACKER -> defend()
        }
    }

    private fun defend(): Action {
        val threadMe = spiders
            .filter { it.threatFor == Player.ME }

        if (Board.playerBase(owner).mana > Action.Wind.COST && threadMe.count { it.coordinate.distanceFrom(coordinate) <= Action.Wind.RANGE } >= 2) {
            return Action.Wind(Vector(coordinate, Board.opponentBase(Player.ME).coordinate))
        }

        return spiders
            .filter { it.threatFor == Player.ME }
            .filter { it.coordinate.distanceFrom(coordinate) < 6000 || it.nearBase }
            .minByOrNull { Board.playerBase(owner).coordinate.distanceFrom(it.coordinate) }
            ?.let { Action.Move(it.coordinate) }
            ?: Action.Move(bestPosition)
    }

    private fun middle(): Action {
        return Action.Move(bestPosition)
    }

    private fun attacker(): Action {
        return Action.Move(bestPosition)
    }


    private fun debug() {
        System.err.println(
            """
            Hero : $id
            Role : $role $bestPosition
            """.trimIndent()
        )
    }

    enum class Role {
        DEFENDER,
        MIDDLE,
        ATTACKER
    }

}
