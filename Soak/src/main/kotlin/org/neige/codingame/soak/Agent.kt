package org.neige.codingame.soak

import org.neige.codingame.geometry.Coordinate

data class Agent(
    val id: Int,
    val player: Player,
    var coordinate: Coordinate = Coordinate(0, 0),
    val optimalRange: Int,
    val soakingPower: Int,
    var wetness: Int = 0,
    var splashBombs: Int = 0,
    var cooldown: Int = 0,
) {
    fun execute(move: Action.Move?, combat: Action.Combat?) {
        println("$id; ${listOfNotNull(move, combat).joinToString(";")}")
    }
}

enum class Player {
    ME,
    OPPONENT;
}

sealed interface Action {
    data class Move(private val coordinate: Coordinate) : Action {
        override fun toString(): String {
            return "MOVE ${coordinate.x} ${coordinate.y}"
        }
    }

    sealed interface Combat : Action {
        data class Shoot(private val agent: Agent) : Combat {
            override fun toString(): String {
                return "SHOOT ${agent.id}"
            }
        }

        data class Throw(private val coordinate: Coordinate) : Combat {
            override fun toString(): String {
                return "THROW ${coordinate.x} ${coordinate.y}"
            }
        }
    }
}
