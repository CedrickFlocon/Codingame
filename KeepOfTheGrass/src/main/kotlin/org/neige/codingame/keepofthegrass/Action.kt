package org.neige.codingame.keepofthegrass

object Action {

    fun build(tile: Tile): String {
        return "BUILD ${tile.x} ${tile.y}"
    }

    fun move(number: Int, from: Tile, to: Tile): String {
        return "MOVE $number ${from.x} ${from.y} ${to.x} ${to.y}"
    }

    fun spawn(number: Int, tile: Tile): String {
        return "SPAWN $number ${tile.x} ${tile.y}"
    }

    fun wait(): String {
        return "WAIT"
    }

    fun message(text: String): String {
        return "MESSAGE $text"
    }

}
