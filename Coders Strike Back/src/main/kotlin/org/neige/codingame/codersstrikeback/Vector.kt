package org.neige.codingame.codersstrikeback


data class Vector(val x: Double, val y: Double) {
    constructor(a: Coordinate, b: Coordinate) : this(b.x - a.x, b.y - a.y)

    operator fun plus(vector: Vector): Vector {
        return Vector(x + vector.x, y + vector.y)
    }

    operator fun minus(vector: Vector): Vector {
        return Vector(x - vector.x, y - vector.y)
    }

    private operator fun times(vector: Vector): Double {
        return x * vector.x + y * vector.y
    }

    fun inverse(): Vector {
        return Vector(-x, -y)
    }

    fun magnitude(): Double {
        return Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0))
    }

    fun angle(): Double {
        return Math.toDegrees(Math.atan((y) / (x)))
    }

    fun angle(vector: Vector): Double {
        return Math.toDegrees(Math.acos((this * vector) / (magnitude() * vector.magnitude())))
    }

    override fun toString(): String {
        return "x: $x; y: $y; Magnitude:${magnitude()}"
    }

}