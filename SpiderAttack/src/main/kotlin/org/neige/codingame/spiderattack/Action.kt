package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector

sealed interface Action {

    fun command(): String
    val manaCost: Int

    object Wait : Action {
        override fun command(): String {
            return "WAIT"
        }

        override val manaCost: Int
            get() = 0
    }

    class Move(private val coordinate: Coordinate) : Action {
        override fun command() = "MOVE ${coordinate.x} ${coordinate.y}"

        override val manaCost: Int
            get() = 0
    }

    class Wind(private val vector: Vector) : Action {
        companion object {
            const val COST = 10
            const val RANGE = 1280
        }

        override fun command() = "SPELL WIND ${vector.x} ${vector.y}"

        override val manaCost: Int
            get() = COST
    }

}
