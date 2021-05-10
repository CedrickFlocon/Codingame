package org.neige.codingame.totoro

import org.neige.codingame.util.Log

class Player(
    private val board: Board
) {

    var sunPoints = 0
    var score = 0
    var isWaiting = false

    var growCost = mutableMapOf(
        0 to 1,
        1 to 1,
        2 to 3,
        3 to 7
    )
    var potentialSun = 0

    fun actions(): List<Action> {
        val myTrees = board.trees.filter { it.owner == this }
        buildSun(myTrees)

        val buildActions = buildActions(myTrees.filter { !it.isDormant })

        return buildActions
    }

    private fun buildSun(myTrees: List<Tree>) {
        growCost[0] = myTrees.filter { it.size == 0 }.count()
        growCost[1] = Grow.BASE_COST[1]!! + myTrees.filter { it.size == 1 }.count()
        growCost[2] = Grow.BASE_COST[2]!! + myTrees.filter { it.size == 2 }.count()
        growCost[3] = Grow.BASE_COST[3]!! + myTrees.filter { it.size == 3 }.count()
        potentialSun = myTrees.sumBy { it.sunPoint[1]!! }
    }

    private fun buildActions(myActiveTree: List<Tree>): List<Action> {
        return myActiveTree
            .filter { it.size < Tree.MAX_SIZE && sunPoints >= growCost[it.size + 1]!! }
            .map { Grow(it, growCost[it.size + 1]!!) } +
                myActiveTree
                    .filter { it.size == Tree.MAX_SIZE && sunPoints >= Complete.COMPLETE_COST }
                    .map { Complete(it) } +
                myActiveTree
                    .filter { it.size > 0 && sunPoints >= growCost[0]!! }
                    .flatMap { tree ->
                        board.getNeighbors(tree.cell, tree.size)
                            .filter { it.first.tree == null && it.first.richness > 0 }
                            .map { tree to it.first }
                    }
                    .map { Seed(it.first, it.second, growCost[0]!!) } +
                Wait
    }

    fun debug() {
        Log.debug(
            """
                Grow 0: ${growCost[0]}
                Grow 1 : ${growCost[1]}
                Grow 2 : ${growCost[2]}
                Grow 3 : ${growCost[3]}
            """.trimIndent()
        )

        board.trees.filter { it.owner == this }.forEach { Log.debug(it) }
    }

}
