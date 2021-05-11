package org.neige.codingame.totoro

import org.neige.codingame.util.Log
import kotlin.math.roundToInt

sealed class Action(
    val player: Player,
    val sunCost: Int,
    var score: Double = 0.0
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
        Log.debug("MyAction $this")
        println("${command()} ${message ?: QUOTE.random()}")
    }

    open val extraCost: Int = 0

    protected abstract fun command(): String

}

class Wait(player: Player) : Action(player, 0) {

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

    val potentialScore: Int
        get() = tree.nutrients + tree.cell.richnessScore

    override fun command(): String {
        return "COMPLETE ${tree.cellId}"
    }

    override fun toString(): String {
        return """
            ${command()} Score=${score.round(2)}
        """.trimIndent()
    }

}

class Seed(
    player: Player,
    val tree: Tree,
    val cell: Cell
) : Action(player, player.growCost[0]!!) {

    override val extraCost: Int
        get() = sunCost

    override fun command(): String {
        return "SEED ${tree.cellId} ${cell.id}"
    }

    override fun toString(): String {
        return """
            ${command()} Cost=$sunCost ExtraCost=$extraCost Score=${score.round(2)}
        """.trimIndent()
    }

}

class Grow(
    player: Player,
    val tree: Tree
) : Action(player, player.growCost[tree.size + 1]!!) {

    companion object {
        val BASE_COST = mapOf(
            1 to 1,
            2 to 3,
            3 to 7
        )
    }

    val expectedTreeSize: Int
        get() = tree.size + 1

    override val extraCost: Int
        get() = sunCost - BASE_COST[expectedTreeSize]!!

    override fun command(): String {
        return "GROW ${tree.cellId}"
    }

    override fun toString(): String {
        return """
         ${command()} Cost=$sunCost ExtraCost=$extraCost Score=${score.round(2)}
        """.trimIndent()
    }
}

fun Double.round(decimals: Int): Double {
    var multiplier = 1.0
    repeat(decimals) { multiplier *= 10 }
    return (this * multiplier).roundToInt() / multiplier
}