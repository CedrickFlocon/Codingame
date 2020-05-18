package org.neige.codingame.geometry

import kotlin.math.pow
import kotlin.math.sqrt

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

    operator fun times(factor: Int): Vector {
        return Vector(x * factor, y * factor)
    }

    fun magnitude(): Double {
        return sqrt(x.toDouble().pow(2.0) + y.toDouble().pow(2.0))
    }

    override fun toString() = "[${this.x},${this.y}]"
}