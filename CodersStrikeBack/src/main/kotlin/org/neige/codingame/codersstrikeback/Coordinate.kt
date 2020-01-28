package org.neige.codingame.codersstrikeback

data class Coordinate(val x: Double, val y: Double) {

    operator fun minus(scalar: Scalar): Coordinate {
        return Coordinate(x - scalar.x, y - scalar.y)
    }

    operator fun plus(scalar: Scalar): Coordinate {
        return Coordinate(scalar.x + x, scalar.y + y)
    }

    fun distanceFrom(coordinate: Coordinate): Double {
        return Math.sqrt(Math.pow((coordinate.y - y), 2.0) + Math.pow((coordinate.x - x), 2.0))
    }

    fun collinear(point1: Coordinate, point2: Coordinate): Boolean {
        return (y - point1.y) * (x - point2.x) == (y - point2.y) * (x - point1.x)
    }

}