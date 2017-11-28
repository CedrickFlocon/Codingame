package org.neige.codingame.hypersonic


data class Action(val type: String, val x: Int, val y: Int) {

    fun play() {
        println("$type $x $y")
    }

}