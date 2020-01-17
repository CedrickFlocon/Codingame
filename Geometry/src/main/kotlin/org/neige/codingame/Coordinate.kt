package org.neige.codingame

import kotlin.math.pow
import kotlin.math.sqrt

data class Coordinate(val x: Double, val y: Double) {

    fun distanceFrom(coordinate: Coordinate): Double {
        return sqrt((coordinate.y - y).pow(2.0) + (coordinate.x - x).pow(2.0))
    }

}