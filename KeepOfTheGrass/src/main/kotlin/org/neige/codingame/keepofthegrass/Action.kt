package org.neige.codingame.keepofthegrass

sealed interface Action {

    val cost: Int
    fun command(): String

    class Build(val tile: Tile) : Action {
        override val cost: Int = 10

        override fun command() = "BUILD ${tile.x} ${tile.y}"
    }

    class Spawn(val number: Int, val tile: Tile) : Action {
        override val cost: Int = 10

        override fun command() = "SPAWN $number ${tile.x} ${tile.y}"
    }

    class Move(val number: Int, val from: Tile, val to: Tile) : Action {
        override val cost: Int = 0

        override fun command() = "MOVE $number ${from.x} ${from.y} ${to.x} ${to.y}"
    }

    object Wait : Action {
        override val cost: Int = 0

        override fun command() = "WAIT"
    }

    class Message(val text: String) : Action {
        override val cost: Int = 0

        override fun command() = "MESSAGE $text"
    }

}
