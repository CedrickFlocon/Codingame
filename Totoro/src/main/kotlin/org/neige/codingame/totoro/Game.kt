package org.neige.codingame.totoro

import org.neige.codingame.util.Log

class Game(
    val board: Board,
    val me: Player,
    val opponent: Player
) {

    var numberOfPossibleMoves = 0

    fun play() {
        val actions = me.actions()
        assert(actions.size == numberOfPossibleMoves)

        val completeAction = actions.filterIsInstance(Complete::class.java)
        val growAction = actions.filterIsInstance(Grow::class.java)
            .filter { board.day.dayCountDown > 2 - it.tree.size }
        val seedAction = actions.filterIsInstance(Seed::class.java)
            .filter { board.day.dayCountDown > 4 }

        val completeTree = completeAction
            .sortedByDescending { (if (it.tree.tomorrowSpooky) 3 else 0) + it.tree.cell.richness }
            .firstOrNull()
            .takeIf {
                when (board.day.day) {
                    in 0..15 -> me.growCost[3]!! > 11
                    in 15..18 -> me.growCost[3]!! > 9
                    in 18..20 -> me.growCost[3]!! > 8
                    else -> true
                }
            }

        val growSpooky = growAction
            .filter { it.tree.tomorrowSpooky && it.tree.tomorrowSpookySize == 0 }
            .firstOrNull()
            ?.also { Log.debug("Helping spooky $it") }

        val growTree = growAction
            .sortedByDescending {
                board.getNeighbors(it.tree.cell, 3)
                    .filter { it.first.tree?.owner == opponent }
                    .sumByDouble { ((it.first.tree?.size?.toDouble()?.plus(1) ?: 0.0) / it.second) } +
                        it.sunCost + it.tree.cell.richness
            }
            .firstOrNull()

        val seedTree = seedAction
            .sortedBy {
                board.getNeighbors(it.cell, 3)
                    .filter { it.first.tree?.owner == me }
                    .sumByDouble { 1.0 / it.second } +
                        (3 - it.cell.richness)
            }
            .firstOrNull()
            .takeIf { me.growCost[0]!! < 2 && me.potentialSun > me.growCost[0]!! + 1 * 3 }

        val action =
            (completeTree
                ?: growTree
                ?: seedTree)
                ?.takeIf {
                    val spookySize = growSpooky?.tree?.size ?: return@takeIf true
                    val growTo = (it as? Grow)?.tree?.size?.plus(1)
                    val sunPointLeft = me.sunPoints - it.sunCost
                    sunPointLeft >= growSpooky.sunCost + if (growTo == spookySize) 1 else 0
                }
                ?: growSpooky
                ?: Wait

        action.play()
    }

}
