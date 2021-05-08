package org.neige.codingame.totoro

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

        val complete = actions.filterIsInstance(Complete::class.java)
            .sortedByDescending { (if (it.tree.tomorrowSpookySize) 3 else 0) + it.tree.cell.richness }
            .firstOrNull()
            .takeIf {
                when (board.day.day) {
                    in 0..15 -> me.growCost[3]!! > 12
                    in 15..18 -> me.growCost[3]!! > 10
                    in 18..20 -> me.growCost[3]!! > 9
                    else -> true
                }
            }

        val grow = actions.filterIsInstance(Grow::class.java)
            .filter { !it.tree.tomorrowSpookySize } //grow if shadow will be smaller
            .sortedByDescending {
                it.sunCost + it.tree.cell.richness + board.getNeighbors(it.tree.cell, 3).sumByDouble { ((it.first.tree?.size?.toDouble()?.plus(1) ?: 0.0) / it.second) }
            }
            .firstOrNull()
            ?.takeIf { board.day.dayCountDown > 2 - it.tree.size }
            ?.takeIf { board.day.day < 20 || complete == null }

        val seed = actions.filterIsInstance(Seed::class.java)
            .sortedBy {
                board.getNeighbors(it.cell, 3).sumByDouble { ((it.first.tree?.size?.toDouble()?.plus(1) ?: 0.0) / it.second) } + (3 - it.cell.richness)
            }
            .firstOrNull()
            .takeIf { board.day.dayCountDown > 4 }
            .takeIf { me.growCost[0]!! < 2 && me.potentialSun > me.growCost[0]!! + 1 * 3 }

        val action = seed
            ?: grow
            ?: complete
            ?: Wait

        action.play()
    }

}
