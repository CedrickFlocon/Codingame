package org.neige.codingame.totoro

import org.neige.codingame.totoro.state.Cell
import org.neige.codingame.totoro.state.Player
import org.neige.codingame.totoro.state.Tree

sealed class Action(
    val player: Player,
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

    open val extraCost = 0

    fun play(message: String? = null) {
        println("${command()} ${message ?: QUOTE.random()}")
    }

    protected abstract fun command(): String

}

class Wait(
    player: Player
) : Action(player, 0) {

    override fun command(): String {
        return "WAIT"
    }

    override fun toString(): String {
        return """
            ${command()}
        """.trimIndent()
    }

}

class Complete(
    player: Player,
    val tree: Tree
) : Action(player, COMPLETE_COST) {

    companion object {
        const val COMPLETE_COST = 4
    }

    override fun command(): String {
        return "COMPLETE ${tree.cell.id}"
    }

    override fun toString(): String {
        return """
            ${command()}
        """.trimIndent()
    }

}

class Grow(
    player: Player,
    sunCost: Int,
    val tree: Tree
) : Action(player, sunCost) {

    companion object {
        val BASE_COST = mapOf(
            1 to 1,
            2 to 3,
            3 to 7
        )
    }

    override val extraCost: Int
        get() = super.sunCost - BASE_COST[tree.size + 1]!!

    override fun command(): String {
        return "GROW ${tree.cell.id}"
    }

    override fun toString(): String {
        return """
         ${command()} Cost=$sunCost
        """.trimIndent()
    }
}

class Seed(
    player: Player,
    sunCost: Int,
    val tree: Tree,
    val cell: Cell,
    val distance: Int
) : Action(player, sunCost) {

    override val extraCost: Int
        get() = super.sunCost

    override fun command(): String {
        return "SEED ${tree.cell.id} ${cell.id}"
    }

    override fun toString(): String {
        return """
            ${command()} Cost=$sunCost Distance=$distance
        """.trimIndent()
    }

}
