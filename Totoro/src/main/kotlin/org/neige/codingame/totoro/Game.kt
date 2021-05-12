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
        val possibleMove = me.actions()
        val opponentActions = opponent.actions()

        assert(possibleMove.size == numberOfPossibleMoves)

        debug()

        buildScore(opponentActions)
        val opponentAction = selectAction(opponentActions)
        opponentAction.takeIf { it !is Seed }?.let { board.play(opponentAction) }

        buildScore(possibleMove)

        Log.debug("OpAction : $opponentAction")
        selectAction(possibleMove).play()
    }

    private fun buildScore(actions: List<Action>) {
        actions.filterIsInstance(Complete::class.java)
            .forEach { action ->
                action.score = (1..board.day.dayCountDown).map { day ->
                    val playerSunPoint = board.trees.filter { it.owner == action.player }
                        .filter { it.spookyBy[day]!!.size == 1 && it.spookyBy[day]!!.first() == action.tree }
                        .sumBy { it.size }

                    val opponentSunPoint = board.trees.filter { it.owner != action.player }
                        .filter { it.spookyBy[day]!!.size == 1 && it.spookyBy[day]!!.first() == action.tree }
                        .sumBy { it.size }

                    ((playerSunPoint - opponentSunPoint - action.tree.sunPoint[day]!!).toDouble() / day.toDouble().pow(2)).also {
                        //Log.debug("(playerSunPoint - $opponentSunPoint - ${action.tree.sunPoint[day]!!}) * ${day.toDouble().pow(2)} = $it")
                    }
                }.sum()
                //Log.debug(action)
            }

        actions.filterIsInstance(Grow::class.java)
            .onEach { action ->
                action.score = (1..board.day.dayCountDown).map { day ->
                    val opponentSunAvoid = board.getNeighborsInSunDirection(action.tree.cell, board.day.sunDirectionIn(day), action.tree.size + 1)
                        .mapNotNull { it.first.tree }
                        .filter { it.owner != action.player }
                        .filter { it.spookyBy[day]!!.isEmpty() && it.size <= action.expectedTreeSize }
                        .sumBy { it.size }

                    val playerSunAvoid = board.getNeighborsInSunDirection(action.tree.cell, board.day.sunDirectionIn(day), action.expectedTreeSize)
                        .mapNotNull { it.first.tree }
                        .filter { it.owner == action.player }
                        .filter { it.spookyBy[day]!!.isEmpty() && it.size <= action.expectedTreeSize }
                        .sumBy { it.size }

                    val treeSunDiff = ((action.expectedTreeSize).takeIf { action.tree.spookySize[day] ?: 0 <= 0 } ?: 0) - action.tree.sunPoint[day]!!

                    ((treeSunDiff + opponentSunAvoid - playerSunAvoid).toDouble() / day.toDouble().pow(2)).also {
                        //Log.debug("($treeSunWithAction - $treeSunWithoutAction + $opponentSunAvoid - $playerSunAvoid) / ${day.toDouble().pow(2)} = $it")
                    }
                }.sum() - action.extraCost / action.expectedTreeSize
                //Log.debug(action)
            }

        actions.filterIsInstance(Seed::class.java)
            .onEach { action ->
                val treeNumber = (0 until Board.MAX_DIRECTION)
                    .flatMap { board.getNeighborsInSunDirection(action.cell, it, Tree.MAX_SIZE) }
                    .filter { it.first.tree?.owner == action.player }
                    .count()

                action.score = (action.cell.richness - treeNumber.toDouble().pow(2.0))
                //Log.debug(action)
                //Log.debug("${action.cell.richness} - ${treeNumber.toDouble().pow(2.0)} = ${action.score}")
            }
    }

    private fun selectAction(actions: List<Action>): Action {
        val completeAction = actions.filterIsInstance(Complete::class.java)
        val growAction = actions.filterIsInstance(Grow::class.java).filter { board.day.dayCountDown > 2 - it.tree.size }
        val seedAction = actions.filterIsInstance(Seed::class.java).filter { board.day.dayCountDown > 4 }

        val completeTree = completeAction.maxBy { it.score }
            ?.takeIf {
                when (board.day.day) {
                    in 0..15 -> me.growCost[Tree.MAX_SIZE]!! > 11
                    in 15..18 -> me.growCost[Tree.MAX_SIZE]!! > 9
                    in 18..20 -> me.growCost[Tree.MAX_SIZE]!! > 8
                    else -> true
                }
            }

        val growTree = growAction.maxBy { it.score }

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
            ?: Wait(me)
        return action
    }

    private fun debug() {
        Log.debug("day : ${board.day.day} nutrients : ${board.nutrients}")
        with(board.trees.filter { it.owner == me }) { Log.debug("Me ${(0..Tree.MAX_SIZE).joinToString { size -> "$size:${this.count { it.size == size }}" }}") }
        with(board.trees.filter { it.owner == opponent }) { Log.debug("Op ${(0..Tree.MAX_SIZE).joinToString { size -> "$size:${this.count { it.size == size }}" }}") }
    }

}
