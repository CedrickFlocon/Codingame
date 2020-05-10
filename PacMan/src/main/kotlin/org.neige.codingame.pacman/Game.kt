package org.neige.codingame.pacman

class Game(private val pacs: Array<Pac>, private val pellets: Array<Pellet>) {

    fun play() {
        val moves = pacs
                .filter { it.team == Pac.Team.ALLY }
                .map { pac -> pac.move(pellets.minBy { it.coordinate.distanceFrom(pac.coordinate) }!!.coordinate) }

        println(moves.joinToString("| "))
    }

}
