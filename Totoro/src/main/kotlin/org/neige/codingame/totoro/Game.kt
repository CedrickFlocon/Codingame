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
        possibleMove.filter { it !is Wait && it !is Seed }.sortedByDescending { it.score }.forEach { Log.debug(it) }

        selectAction(possibleMove).play()
    }

    private fun buildScore(actions: List<Action>) {
        actions.filterIsInstance(Complete::class.java)
            .forEach { action ->
                val sunImpact = (1..board.day.countDown).map { day ->
                    val playerSunPoint = board.trees.filter { it.owner == action.player }
                        .filter { it.spookyBy[day]!!.size == 1 && it.spookyBy[day]!!.first() == action.tree }
                        .sumBy { it.size }

                    val opponentSunPoint = board.trees.filter { it.owner != action.player }
                        .filter { it.spookyBy[day]!!.size == 1 && it.spookyBy[day]!!.first() == action.tree }
                        .sumBy { it.size }

                    ((playerSunPoint - opponentSunPoint - action.tree.sunPoint[day]!!).toDouble() / day.toDouble().pow(2))
                }.sum()
                action.score = sunImpact * board.day.countDownPercentage
            }

        actions.filterIsInstance(Grow::class.java)
            .onEach { action ->
                val sunImpact = (1..board.day.countDown).map { day ->
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

                    ((treeSunDiff + opponentSunAvoid - playerSunAvoid).toDouble() / day.toDouble().pow(2))
                }.sum()
                action.score = (sunImpact * board.day.countDownPercentage) - (action.extraCost.toDouble() / action.expectedTreeSize.toDouble())
            }

        actions.filterIsInstance(Seed::class.java)
            .onEach { action ->
                val treeNumber = (0 until Board.MAX_DIRECTION)
                    .flatMap { board.getNeighborsInSunDirection(action.cell, it, Tree.MAX_SIZE) }
                    .filter { it.first.tree?.owner == action.player }
                    .count()

                action.score = (action.cell.richness - treeNumber.toDouble().pow(2.0))
            }
    }

    private fun selectAction(actions: List<Action>): Action {
        val completeAction = actions.filterIsInstance(Complete::class.java).filter { it.potentialScore > 0 }
        val growAction = actions.filterIsInstance(Grow::class.java).filter { board.day.countDown > 2 - it.tree.size }
        val seedAction = actions.filterIsInstance(Seed::class.java).filter { board.day.countDown > 4 }

        val lastSeed = actions.filterIsInstance(Seed::class.java)
            .firstOrNull()
            ?.takeIf { board.day.countDown == 0 && me.sunPoints % 3 >= it.sunCost }

        val seedTree = seedAction
            .sortedByDescending { it.score }
            .filter { it.score >= 0.0 }
            .firstOrNull()
            ?.takeIf { it.extraCost == 0 && me.potentialSun > 3 }

        val action = (completeAction + growAction).maxBy { it.score }
            ?: seedTree ?: lastSeed
            ?: Wait(me)
        return action
    }

    private fun debug() {
        Log.debug("day : ${board.day.day} nutrients : ${board.nutrients}")
        with(board.trees.filter { it.owner == me }) { Log.debug("Me ${(0..Tree.MAX_SIZE).joinToString { size -> "$size:${this.count { it.size == size }}" }}") }
        with(board.trees.filter { it.owner == opponent }) { Log.debug("Op ${(0..Tree.MAX_SIZE).joinToString { size -> "$size:${this.count { it.size == size }}" }}") }
    }


    private fun sunToScore(sun: Double): Double {
        return sun / 3
    }
}
