package org.neige.codingame.totoro

sealed class Action {

    fun play() {
        println(command())
    }

    protected abstract fun command(): String

}

data class Wait(
    private val message: String? = null
) : Action() {

    override fun command(): String {
        return "WAIT $message"
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
