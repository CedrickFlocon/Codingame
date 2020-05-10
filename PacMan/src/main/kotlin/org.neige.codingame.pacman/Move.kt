package org.neige.codingame.pacman

class Move(private val pac: Pac, private val coordinate: Coordinate) {

    override fun toString() = "MOVE ${pac.id} ${coordinate.x} ${coordinate.y}"

}
