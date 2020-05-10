package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate

data class Pellet(
        val value: Int,
        val coordinate: Coordinate
) {

    companion object {
        const val SUPER_PELLETS = 10
    }

}
