package org.neige.codingame.pacman

class Pac(
        val id: Int,
        val team: Team,
        val coordinate: Coordinate
) {
    fun move(coordinate: Coordinate) = Move(this, coordinate)

    enum class Team {
        ALLY,
        ENEMY
    }

}
