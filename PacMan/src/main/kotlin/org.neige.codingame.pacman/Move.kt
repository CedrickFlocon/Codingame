package org.neige.codingame.pacman

class Move(private val coordinate: Coordinate) {

    override fun toString() = "MOVE 0 ${coordinate.x} ${coordinate.y}"

}
