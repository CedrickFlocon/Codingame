package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate

class Base(
    val coordinate: Coordinate,
    val player: Player
) {

    companion object {
        const val VISIBILITY = 6000
    }

    var health: Int = 3
    var mana: Int = 0

    fun play(action: Action) {
        println(action.command())
        mana -= action.manaCost
    }

}
