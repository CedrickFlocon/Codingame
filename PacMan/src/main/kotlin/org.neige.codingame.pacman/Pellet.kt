package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate
import java.util.Scanner

class Pellet(
        val value: Int,
        override val coordinate: Coordinate,
        override var lastTurnSeen: Int
) : Element {

    companion object {
        const val SUPER_PELLETS = 10

        fun readPellet(input: Scanner): Pellet {
            val x = input.nextInt()
            val y = input.nextInt()
            val value = input.nextInt()

            return Pellet(value, Coordinate(x, y), 0)
        }
    }

    override fun toString(): String {
        return "Pellet $coordinate"
    }
}
