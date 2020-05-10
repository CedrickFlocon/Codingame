package org.neige.codingame.pacman

class Game(private val pac: Pac, private val pellets: Array<Pellet>) {

    fun play() {
        println(Move(pellets.minBy { it.coordinate.distanceFrom(pac.coordinate) }!!.coordinate))
    }

}