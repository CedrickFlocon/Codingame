package org.neige.codingame.codersstrikeback


data class Scalar(val x: Double, val y: Double) {
    constructor(a: Coordinate, b: Coordinate) : this(b.x - a.x, b.y - a.y)

    operator fun plus(scalar: Scalar): Scalar {
        return Scalar(x + scalar.x, y + scalar.y)
    }

    operator fun minus(scalar: Scalar): Scalar {
        return Scalar(x - scalar.x, y - scalar.y)
    }

    private operator fun times(scalar: Scalar): Double {
        return x * scalar.x + y * scalar.y
    }

    operator fun times(friction: Double): Scalar {
        return Scalar(x * friction, y * friction)
    }

    operator fun div(proportion: Double): Scalar {
        return Scalar(x / proportion, y / proportion)
    }

    fun inverse(): Scalar {
        return Scalar(-x, -y)
    }

    fun magnitude(): Double {
        return Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0))
    }

    fun angle(): Double {
        return Math.toDegrees(Math.atan((y) / (x)))
    }

    fun angle(scalar: Scalar): Double {
        return Math.toDegrees(Math.acos((this * scalar) / (magnitude() * scalar.magnitude())))
    }

    override fun toString(): String {
        return "x: $x; y: $y; Magnitude:${magnitude()}"
    }

}