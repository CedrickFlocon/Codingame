package org.neige.codingame.hypersonic


data class Action(val type: Command, val located: Located, val message: String = "") {

    fun play() {
        println("${type.name} ${located.x} ${located.y} $message".trim())
    }

    enum class Command {
        BOMB,
        MOVE
    }

}