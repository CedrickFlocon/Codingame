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
        if (actions.size != numberOfPossibleMoves) {
            throw RuntimeException("Number of possible moves not equals ${actions.size} != $numberOfPossibleMoves")
        }

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
            .filter { !it.tree.tomorrowSpooky }
            .sortedByDescending {
                board.getNeighbors(it.tree.cell, 3).sumByDouble { ((it.first.tree?.size?.toDouble()?.plus(1) ?: 0.0) / it.second) } +
                        it.sunCost + it.tree.cell.richness
            }
            .firstOrNull()
            ?.takeIf { board.day.day < 20 || completeTree == null }

        val seedTree = seedAction
            .sortedBy {
                board.getNeighbors(it.cell, 3)
                    .sumByDouble { ((it.first.tree?.size?.toDouble()?.plus(1) ?: 0.0) / it.second) } +
                        (3 - it.cell.richness)
            }
            .firstOrNull()
            .takeIf { me.growCost[0]!! < 2 && me.potentialSun > me.growCost[0]!! + 1 * 3 }

        val action = growSpooky
            ?: seedTree
            ?: completeTree
            ?: growTree
            ?: Wait

        action.play()
    }

}
