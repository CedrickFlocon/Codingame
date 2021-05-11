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
                action.score = (1..board.day.dayCountDown).map { day ->
                    val meSunPoint = board.trees.filter { it.owner == me }
                        .filter { it.spookyBy[day]!!.size == 1 && it.spookyBy[day]!!.first() == action.tree }
                        .sumBy { it.size }

                    val opponentSunPoint = board.trees.filter { it.owner == opponent }
                        .filter { it.spookyBy[day]!!.size == 1 && it.spookyBy[day]!!.first() == action.tree }
                        .sumBy { it.size }

                    ((meSunPoint - opponentSunPoint - action.tree.sunPoint[day]!!).toDouble() / day.toDouble().pow(2)).also {
                        //Log.debug("($meSunPoint - $opponentSunPoint - ${action.tree.sunPoint[day]!!}) * ${day.toDouble().pow(2)} = $it")
                    }
                }.sum()
                //Log.debug(action)
            }
        val growAction = actions.filterIsInstance(Grow::class.java)
            .filter { board.day.dayCountDown > 2 - it.tree.size }
            .onEach { action ->
                action.score = (1..board.day.dayCountDown).map { day ->
                    val opponentSunAvoid = board.getNeighborsInSunDirection(action.tree.cell, board.day.sunDirectionIn(day), action.tree.size + 1)
                        .mapNotNull { it.first.tree }
                        .filter { it.owner == opponent }
                        .filter { it.spookyBy[day]!!.isEmpty() && it.size <= action.expectedTreeSize }
                        .sumBy { it.size }

                    val meSunAvoid = board.getNeighborsInSunDirection(action.tree.cell, board.day.sunDirectionIn(day), action.expectedTreeSize)
                        .mapNotNull { it.first.tree }
                        .filter { it.owner == me }
                        .filter { it.spookyBy[day]!!.isEmpty() && it.size <= action.expectedTreeSize }
                        .sumBy { it.size }

                    val treeSunWithoutAction = action.tree.sunPoint[day]!!
                    val treeSunWithAction = (action.expectedTreeSize).takeIf { action.tree.spookySize[day] ?: 0 <= 0 } ?: 0

                    ((treeSunWithAction - treeSunWithoutAction + opponentSunAvoid - meSunAvoid).toDouble() / day.toDouble().pow(2)).also {
                        //Log.debug("($treeSunWithAction - $treeSunWithoutAction + $opponentSunAvoid - $meSunAvoid) / ${day.toDouble().pow(2)} = $it")
                    }
                }.sum()
                //Log.debug("==>${action.score}")
            }
        val seedAction = actions.filterIsInstance(Seed::class.java)
            .filter { board.day.dayCountDown > 4 }
            .onEach { action ->
                val treeNumber = (0 until Board.MAX_DIRECTION)
                    .flatMap { board.getNeighborsInSunDirection(action.cell, it, Tree.MAX_SIZE) }
                    .filter { it.first.tree?.owner == me }
                    .count()

                action.score = (action.cell.richness - treeNumber.toDouble().pow(2.0))
                //Log.debug(action)
                //Log.debug("${action.cell.richness} - ${treeNumber.toDouble().pow(2.0)} = ${action.score}")
            }


        val completeTree = completeAction
            .groupBy { it.score }.maxBy { it.key }?.value
            ?.sortedByDescending { it.tree.cell.richness }
            ?.firstOrNull()
            ?.takeIf {
                when (board.day.day) {
                    in 0..15 -> me.growCost[Tree.MAX_SIZE]!! > 11
                    in 15..18 -> me.growCost[Tree.MAX_SIZE]!! > 9
                    in 18..20 -> me.growCost[Tree.MAX_SIZE]!! > 8
                    else -> true
                } || (it.score > 0 && me.potentialSun > 8)
            }

        val growTree = growAction
            .groupBy { it.score }.maxBy { it.key }?.value
            ?.groupBy { it.extraCost / (it.expectedTreeSize + 1) }?.minBy { it.key }?.value
            ?.sortedByDescending { it.tree.cell.richness }
            ?.firstOrNull()
            ?.takeIf { board.day.day < 20 || completeTree == null }
        val lastSeed = actions.filterIsInstance(Seed::class.java)
            .firstOrNull()
            ?.takeIf { board.day.dayCountDown == 0 && me.sunPoints % 3 >= it.sunCost }

        val seedTree = seedAction
            .sortedByDescending { it.score }
            .filter { it.score >= 0.0 }
            .firstOrNull()
            ?.takeIf { it.extraCost == 0 && me.potentialSun > 3 }

        val action = completeTree
            ?: growTree
            ?: seedTree ?: lastSeed
            ?: Wait

        action.play()
    }

    private fun debug() {
        Log.debug("day : ${board.day.day} nutrients : ${board.nutrients}")
        with(board.trees.filter { it.owner == me }) { Log.debug("Me ${(0..Tree.MAX_SIZE).joinToString { size -> "$size:${this.count { it.size == size }}" }}") }
        with(board.trees.filter { it.owner == opponent }) { Log.debug("Op ${(0..Tree.MAX_SIZE).joinToString { size -> "$size:${this.count { it.size == size }}" }}") }
    }

}
