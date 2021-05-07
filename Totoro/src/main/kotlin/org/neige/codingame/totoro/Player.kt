package org.neige.codingame.totoro

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

        return buildActions(myTrees.filter { !it.isDormant })
    }

    private fun buildSun(myTrees: List<Tree>) {
        growCost[0] = 0 + myTrees.filter { it.size == 0 }.count()
        growCost[1] = 1 + myTrees.filter { it.size == 1 }.count()
        growCost[2] = 3 + myTrees.filter { it.size == 2 }.count()
        growCost[3] = 7 + myTrees.filter { it.size == 3 }.count()
        potentialSun = myTrees.filter { !it.tomorrowSpookySize }.sumBy { it.size }
    }

    private fun buildActions(myActiveTree: List<Tree>): List<Action> {
        return myActiveTree
            .filter { it.size < 3 && sunPoints >= growCost[it.size + 1]!! }
            .map { Grow(it, growCost[it.size + 1]!!) } +
                myActiveTree
                    .filter { it.size == 3 && sunPoints >= Complete.COMPLETE_COST }
                    .map { Complete(it) } +
                myActiveTree
                    .filter { it.size > 0 && sunPoints >= growCost[0]!! }
                    .flatMap { tree ->
                        board.getNeighbors(tree.cell, tree.size)
                            .filter { it.tree == null && it.richness > 0 }
                            .map { tree to it }
                    }
                    .map { Seed(it.first, it.second, growCost[0]!!) } +
                Wait
    }

}
