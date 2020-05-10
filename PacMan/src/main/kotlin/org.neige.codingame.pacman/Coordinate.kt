package org.neige.codingame.pacman

import kotlin.math.pow
import kotlin.math.sqrt


data class Coordinate(val x: Int, val y: Int) {

    fun distanceFrom(coordinate: Coordinate): Double {
        return sqrt((coordinate.y - y).toDouble().pow(2.0) + (coordinate.x - x).toDouble().pow(2.0))
    }

}