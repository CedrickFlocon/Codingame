package org.neige.codingame.totoro

import org.neige.codingame.util.Log

sealed class Action(
    val sunCost: Int
) {

    companion object {
        private val QUOTE = listOf(
            "Ready to work.",
            "Yes?",
            "Hmmm?",
            "What you want?",
            "Something need doing?",
            "I can do that.",
            "Be happy to.",
            "Work, work.",
            "Okie dokie.",

            "Ready to work.",
            "Yes, milord?",
            "What is it?",
            "More work?",
            "What?",
            "Right-o.",
            "Yes, milord.",
            "All right.",
            "Off I go, then!",

            "I stand ready.",
            "Waiting on you.",
            "Point the way.",
            "On your mark.",
            "Your move.",
            "Say no more.",
            "Done.",
            "Fair enough.",
            "All too easy."
        )
    }

    fun play(message: String? = null) {
        Log.debug("Played action $this")
        println("${command()} ${message ?: QUOTE.random()}")
    }

    protected abstract fun command(): String

}

object Wait : Action(0) {

    override fun command(): String {
        return "WAIT"
    }

}

class Complete(
    val tree: Tree
) : Action(COMPLETE_COST) {

    companion object {
        const val COMPLETE_COST = 4
    }

    override fun command(): String {
        return "COMPLETE ${tree.cellId}"
    }

    override fun toString(): String {
        return """
            ${command()}
            $tree
        """.trimIndent()
    }

}

class Seed(
    val tree: Tree,
    val cell: Cell,
    sunCost: Int
) : Action(sunCost) {

    override fun command(): String {
        return "SEED ${tree.cellId} ${cell.id}"
    }

    override fun toString(): String {
        return """
            ${command()}
            $tree
            $cell
            $sunCost
        """.trimIndent()
    }

}

class Grow(
    val tree: Tree,
    sunCost: Int
) : Action(sunCost) {

    override fun command(): String {
        return "GROW ${tree.cellId}"
    }

    override fun toString(): String {
        return """
            ${command()}
            $tree
            $sunCost
        """.trimIndent()
    }
}
