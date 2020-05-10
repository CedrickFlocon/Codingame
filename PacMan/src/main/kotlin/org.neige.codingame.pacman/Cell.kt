package org.neige.codingame.pacman

interface Cell {
    val coordinate: Coordinate
}

class Floor(override val coordinate: Coordinate) : Cell
class Wall(override val coordinate: Coordinate) : Cell
