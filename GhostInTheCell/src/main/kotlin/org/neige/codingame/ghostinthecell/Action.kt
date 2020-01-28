package org.neige.codingame.ghostinthecell

abstract class Action {
    abstract fun play(): String
}

object Waiting : Action() {

    override fun play(): String {
        return "WAIT"
    }

}

class Messaging(private val message: String) : Action() {

    override fun play(): String {
        return "MSG $message"
    }

}
