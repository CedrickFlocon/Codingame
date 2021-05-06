package org.neige.codingame.totoro

sealed class Action(
) {

    fun play(message: String? = null) {
        println("${command()} ${message ?: ""}")
    }

    protected abstract fun command(): String

}

class Wait() : Action() {

    override fun command(): String {
        return "WAIT"
    }

}

class Complete(
    private val cellId: Int
) : Action() {

    override fun command(): String {
        return "COMPLETE $cellId"
    }

}

class Seed(
    private val sourceId: Int,
    private val targetId: Int
) : Action() {

    override fun command(): String {
        return "SEED $sourceId $targetId"
    }

}

class Grow(
    private val cellId: Int
) : Action() {

    override fun command(): String {
        return "GROW $cellId"
    }

}
