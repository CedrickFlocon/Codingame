package org.neige.codingame.soak

import kotlin.math.abs

data class Coordinate(val x: Int, val y: Int) {

    operator fun plus(vector: Vector): Coordinate {
        return Coordinate(x + vector.x, y + vector.y)
    }

    operator fun minus(vector: Vector): Coordinate {
        return Coordinate(x - vector.x, y - vector.y)
    }

    /**
     * Manhattan distance
     */
    fun distanceFrom(coordinate: Coordinate): Int {
        return abs(coordinate.x - x) + abs(coordinate.y - y)
    }

    override fun toString() = "[${this.x},${this.y}]"
}

data class Vector(val x: Int, val y: Int) {

    operator fun unaryMinus(): Vector {
        return Vector(-x, -y)
    }

    operator fun plus(vector: Vector): Vector {
        return Vector(x + vector.x, y + vector.y)
    }

    operator fun minus(vector: Vector): Vector {
        return Vector(x - vector.x, y - vector.y)
    }

    override fun toString() = "[${this.x},${this.y}]"
}
