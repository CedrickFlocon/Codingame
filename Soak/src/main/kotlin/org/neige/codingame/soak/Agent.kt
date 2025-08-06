package org.neige.codingame.soak

import org.neige.codingame.geometry.Coordinate

data class Agent(
    val id: Int,
    val player: Player,
    var coordinate: Coordinate = Coordinate(0, 0),
    val optimalRange: Int,
    val soakingPower: Int,
    var wetness: Int = 0
) {
    fun execute(actions: List<Action>) {
        println("$id; ${actions.joinToString(";")}")
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

    data class Shoot(private val agent: Agent) : Action {
        override fun toString(): String {
            return "SHOOT ${agent.id}"
        }
    }
}
