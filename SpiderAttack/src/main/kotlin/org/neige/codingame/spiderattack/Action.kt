package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate

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

    sealed interface Spell : Action {
        companion object {
            const val COST = 10
        }

        override val manaCost: Int
            get() = COST

        class Wind(private val coordinate: Coordinate) : Spell {
            companion object {
                const val RANGE = 1280
                const val MOVE = 2200
            }

            override fun command() = "SPELL WIND ${coordinate.x} ${coordinate.y}"
        }

        class Shield(private val entity: Entity) : Spell {
            companion object {
                const val RANGE = 2200
            }

            override fun command() = "SPELL SHIELD ${entity.id}"
        }

        class Control(
            private val entity: Entity,
            private val coordinate: Coordinate
        ) : Spell {
            companion object {
                const val RANGE = 2200
            }

            override fun command() = "SPELL CONTROL ${entity.id} ${coordinate.x} ${coordinate.y}"
        }

    }

}
