package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate

class Pellet(
        val value: Int,
        override val coordinate: Coordinate,
        var lastTurnSeen: Int
) : Element {

    companion object {
        const val SUPER_PELLETS = 10
    }

    override fun toString(): String {
        return "Pellet $coordinate"
    }
}
