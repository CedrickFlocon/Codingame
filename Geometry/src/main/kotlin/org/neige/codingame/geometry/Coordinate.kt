package org.neige.codingame.geometry

import kotlin.math.pow
import kotlin.math.sqrt

data class Coordinate(val x: Int, val y: Int) {

    operator fun plus(vector: Vector): Coordinate {
        return Coordinate(x + vector.x, y + vector.y)
    }

    operator fun minus(vector: Vector): Coordinate {
        return Coordinate(x - vector.x, y - vector.y)
    }

    fun distanceFrom(coordinate: Coordinate): Double {
        return sqrt((coordinate.y - y).toDouble().pow(2.0) + (coordinate.x - x).toDouble().pow(2.0))
    }

    override fun toString() = "[${this.x},${this.y}]"
}

operator fun <T> Array<Array<T>>.get(coordinate: Coordinate): T {
    return this[coordinate.x][coordinate.y]
}

operator fun <T> Array<Array<T>>.set(coordinate: Coordinate, value: T) {
    this[coordinate.x][coordinate.y] = value
}
