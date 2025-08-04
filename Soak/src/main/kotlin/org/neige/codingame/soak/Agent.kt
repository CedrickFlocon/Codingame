package org.neige.codingame.soak

import org.neige.codingame.geometry.Coordinate

data class Agent(
    val id: Int,
    val player: Player
) {

    fun move(coordinate: Coordinate) {
        println("$id; MOVE ${coordinate.x} ${coordinate.y}")
    }

}

enum class Player {
    ME,
    OPPONENT
}
