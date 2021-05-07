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

        val complete = actions.filterIsInstance(Complete::class.java) //move linear complete tree
            .sortedByDescending { it.tree.cell.richness }
            .filter {
                when (board.day.day) {
                    in 0..20 -> {
                        if (it.tree.tomorrowSpookySize) {
                            me.growCost[3]!! > 10
                        } else {
                            me.growCost[3]!! > 13
                        }
                    }
                    else -> true
                }
            }
            .firstOrNull()

        val grow = actions.filterIsInstance(Grow::class.java)
            .filter { !it.tree.tomorrowSpookySize } //grow if shadow will be smaller
            .sortedByDescending { it.sunCost + it.tree.cell.richness }
            .firstOrNull()
            ?.takeIf { board.day.dayCountDown > 2 - it.tree.size }
            ?.takeIf { board.day.day < 20 || complete == null }

        val seed = actions.filterIsInstance(Seed::class.java)
            .sortedBy {
                board.getNeighbors(it.cell, 1).sumBy { it.tree?.size ?: 0 }.toDouble() + (3 - it.cell.richness) //check distance 3 sum(treesize / distance)
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
