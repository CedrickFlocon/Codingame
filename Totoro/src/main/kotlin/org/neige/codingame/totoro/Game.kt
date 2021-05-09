package org.neige.codingame.totoro

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
            .onEach { action ->
                val meSunPoint = board.trees.filter { it.owner == me }
                    .filter { it.tomorrowSpookyBy.size == 1 && it.tomorrowSpookyBy.first() == action.tree }
                    .sumBy { it.size }

                val opponentSunPoint = board.trees.filter { it.owner == opponent }
                    .filter { it.tomorrowSpookyBy.size == 1 && it.tomorrowSpookyBy.first() == action.tree }
                    .sumBy { it.size }

                val treeSunPoint = action.tree.tomorrowSunPoint
                action.score = (meSunPoint - opponentSunPoint - treeSunPoint).toDouble()
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

                val growCostIncrease = (action.growCostIncrease).toDouble() / action.expectedTreeSize

                action.score = (treeSunWithAction - treeSunWithoutAction + opponentSunAvoid - meSunAvoid).toDouble() - growCostIncrease
            }
        val seedAction = actions.filterIsInstance(Seed::class.java)
            .filter { board.day.dayCountDown > 4 }
            .onEach { action ->
                action.score = board.getNeighbors(action.cell, 3)
                    .filter { it.first.tree?.owner == me }
                    .sumByDouble { it.second.toDouble() - 3 } + action.cell.richness
            }


        val completeTree = completeAction
            .sortedByDescending { it.score }
            .firstOrNull()
            ?.takeIf {
                when (board.day.day) {
                    in 0..15 -> me.growCost[3]!! > 11
                    in 15..18 -> me.growCost[3]!! > 9
                    in 18..20 -> me.growCost[3]!! > 8
                    else -> true
                }
            }

        val growTree = growAction
            .sortedByDescending { it.score }
            .firstOrNull()
            ?.takeIf { board.day.day < 20 || completeTree == null }

        val seedTree = seedAction
            .sortedByDescending { it.score }
            .firstOrNull()
            .takeIf { me.growCost[0]!! < 2 && me.potentialSun > me.growCost[0]!! + 1 * 3 }

        val action =
            completeTree
                ?: growTree
                ?: seedTree
                ?: Wait

        action.play()
    }

}
