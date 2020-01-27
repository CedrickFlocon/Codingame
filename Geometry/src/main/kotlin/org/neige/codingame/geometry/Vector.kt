package org.neige.codingame.geometry

import kotlin.math.pow
import kotlin.math.sqrt

data class Vector(val x: Double, val y: Double) {

    operator fun unaryMinus(): Vector {
        return Vector(-x, -y)
    }

    operator fun plus(vector: Vector): Vector {
        return Vector(x + vector.x, y + vector.y)
    }

    operator fun minus(vector: Vector): Vector {
        return Vector(x - vector.x, y - vector.y)
    }

    operator fun times(factor: Double): Vector {
        return Vector(x * factor, y * factor)
    }

    fun magnitude(): Double {
        return sqrt(x.pow(2.0) + y.pow(2.0))
    }

}