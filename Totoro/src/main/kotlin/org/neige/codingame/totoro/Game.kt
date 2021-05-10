package org.neige.codingame.totoro

import org.neige.codingame.util.Log
import kotlin.math.pow

class Game(
    val board: Board,
    val me: Player,
    val opponent: Player
) {

    var numberOfPossibleMoves = 0

    fun play() {
        val actions = me.actions()
        assert(actions.size == numberOfPossibleMoves)

        debug()

        val completeAction = actions.filterIsInstance(Complete::class.java)
            .onEach { action ->
                val meSunPoint = board.trees.filter { it.owner == me }
                    .filter { it.tomorrowSpookyBy.size == 1 && it.tomorrowSpookyBy.first() == action.tree }
                    .sumBy { it.size }

                val opponentSunPoint = board.trees.filter { it.owner == opponent }
                    .filter { it.tomorrowSpookyBy.size == 1 && it.tomorrowSpookyBy.first() == action.tree }
                    .sumBy { it.size }

                action.score = (meSunPoint - opponentSunPoint - action.tree.tomorrowSunPoint).toDouble()

                //Log.debug("Complete[${action.tree.cellId}] : $meSunPoint - $opponentSunPoint - ${action.tree.tomorrowSunPoint}} = ${action.score}")
            }
        val growAction = actions.filterIsInstance(Grow::class.java)
            .filter { board.day.dayCountDown > 2 - it.tree.size }
            .onEach { action ->
                val opponentSunAvoid = board.getNeighborsInSunDirection(action.tree.cell, board.day.tomorrowSunDirection, action.tree.size + 1)
                    .mapNotNull { it.first.tree }
                    .filter { it.owner == opponent }
                    .filter { it.tomorrowSpookyBy.isEmpty() && it.size <= action.expectedTreeSize }
                    .sumBy { it.size }

                val meSunAvoid = board.getNeighborsInSunDirection(action.tree.cell, board.day.tomorrowSunDirection, action.expectedTreeSize)
                    .mapNotNull { it.first.tree }
                    .filter { it.owner == me }
                    .filter { it.tomorrowSpookyBy.isEmpty() && it.size <= action.expectedTreeSize }
                    .sumBy { it.size }

                val treeSunWithoutAction = action.tree.tomorrowSunPoint
                val treeSunWithAction = (action.expectedTreeSize).takeIf { action.tree.tomorrowSpookySize == null || action.tree.tomorrowSpookySize == 0 } ?: 0

                val growCostIncrease = (action.extraCost).toDouble() / action.expectedTreeSize

                action.score = (treeSunWithAction - treeSunWithoutAction + opponentSunAvoid - meSunAvoid).toDouble() - growCostIncrease
            }
        val seedAction = actions.filterIsInstance(Seed::class.java)
            .filter { board.day.dayCountDown > 4 }
            .onEach { action ->
                val treeNumber = (0 until Board.MAX_DIRECTION)
                    .flatMap { board.getNeighborsInSunDirection(action.cell, it, Tree.MAX_SIZE) }
                    .filter { it.first.tree?.owner == me }
                    .count()

                action.score = (action.cell.richness - treeNumber.toDouble().pow(2.0))
                //Log.debug("Seed[${action.cell.id}] : ${action.cell.richness} - ${treeNumber.toDouble().pow(2.0)} = ${action.score}")
            }


        val completeTree = completeAction
            .sortedByDescending { it.score }
            .firstOrNull()
            ?.takeIf {
                when (board.day.day) {
                    in 0..15 -> me.growCost[Tree.MAX_SIZE]!! > 11
                    in 15..18 -> me.growCost[Tree.MAX_SIZE]!! > 9
                    in 18..20 -> me.growCost[Tree.MAX_SIZE]!! > 8
                    else -> true
                } || it.score > 1
            }

        val growTree = growAction
            .sortedByDescending { it.score }
            .firstOrNull()
            ?.takeIf { board.day.day < 20 || completeTree == null }

        val seedTree = seedAction
            .sortedByDescending { it.score }
            .filter { it.score >= 0.0 }
            .firstOrNull()
            ?.takeIf { it.extraCost == 0 && me.potentialSun > 3 }

        val action = seedTree
            ?: completeTree
            ?: growTree
            ?: Wait

        action.play()
    }

    private fun debug() {
        Log.debug("day : ${board.day.day} nutrients : ${board.nutrients}")
        with(board.trees.filter { it.owner == me }) { Log.debug("Me ${(0..Tree.MAX_SIZE).joinToString { size -> "$size:${this.count { it.size == size }}" }}") }
        with(board.trees.filter { it.owner == opponent }) { Log.debug("Op ${(0..Tree.MAX_SIZE).joinToString { size -> "$size:${this.count { it.size == size }}" }}") }

        Log.debug("Me ${(0..Tree.MAX_SIZE).joinToString { "$it:${me.growCost[it]!!}" }}")
        Log.debug("Op ${(0..Tree.MAX_SIZE).joinToString { "$it:${opponent.growCost[it]!!}" }}")
    }

}
