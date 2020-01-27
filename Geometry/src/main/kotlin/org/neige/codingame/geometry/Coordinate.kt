package org.neige.codingame.geometry

import kotlin.math.pow
import kotlin.math.sqrt

data class Coordinate(val x: Double, val y: Double) {

    operator fun plus(vector: Vector): Coordinate {
        return Coordinate(x + vector.x, y + vector.y)
    }

    fun distanceFrom(coordinate: Coordinate): Double {
        return sqrt((coordinate.y - y).pow(2.0) + (coordinate.x - x).pow(2.0))
    }

}

operator fun <T> Array<Array<T>>.get(coordinate: Coordinate): T {
    return this[coordinate.x.toInt()][coordinate.y.toInt()]
}
