package org.neige.codingame.soak

import org.neige.codingame.geometry.Coordinate

data class Agent(
    val id: Int,
    val player: Player,
    val optimalRange: Int,
    val soakingPower: Int
) {

    var wetness: Int = 0

    fun move(coordinate: Coordinate) {
        println("$id; MOVE ${coordinate.x} ${coordinate.y}")
    }

    fun shoot(agent: Agent) {
        println("$id; SHOOT ${agent.id}")
    }

}

enum class Player {
    ME,
    OPPONENT
}
