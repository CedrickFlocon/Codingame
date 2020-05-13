package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate

interface Cell {
    val coordinate: Coordinate
}

class Floor(override val coordinate: Coordinate) : Cell
class Wall(override val coordinate: Coordinate) : Cell
