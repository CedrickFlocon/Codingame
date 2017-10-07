package org.neige.codingame.codersstrikeback

data class Coordinate(val x: Double, val y: Double) {

    operator fun minus(vector: Vector): Coordinate {
        return Coordinate(x - vector.x, y - vector.y)
    }

    fun distanceFrom(coordinate: Coordinate): Double {
        return Math.sqrt(Math.pow((coordinate.y - y), 2.0) + Math.pow((coordinate.x - x), 2.0))
    }

    fun collinear(checkpoint: Coordinate, position: Coordinate): Boolean {
        return (y - checkpoint.y) * (x - position.x) == (y - position.y) * (x - checkpoint.x)
    }

}