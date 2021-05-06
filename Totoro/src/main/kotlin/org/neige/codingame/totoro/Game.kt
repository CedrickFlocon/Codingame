package org.neige.codingame.totoro

class Game(
    val board: Board,
    val me: Player,
    val opponent: Player
) {

    var nutrients = 0
    var day = 0

    fun nextTurn() {
        me.trees = board.myTrees
        opponent.trees = board.opponentTrees

        play()
    }

    private fun play() {
        val completableTree = me.completableTree()
            .sortedByDescending { it.cell.richness }
            .firstOrNull()

        val groawableTree = me.growableTree()
            .filter { !it.tomorrowSpookySize }
            .sortedByDescending { it.cell.richness }
            .firstOrNull()
            .takeIf { day < 20 || completableTree == null }

        val seedableCell = board
            .myTrees
            .filter { !it.isDormant }
            .flatMap { tree -> board.getNeighbors(tree.cell, tree.size).map { tree to it } }
            .filter { it.second.tree == null }
            .filter { it.second.richness > 0 }
            .sortedByDescending { it.second.richness }
            .firstOrNull()
            .takeIf { me.sunPoints >= me.growCost[0]!! }
            .takeIf { day < 13 }

        val action = groawableTree?.let { Grow(it.cellId) }
            ?: completableTree?.let { Complete(it.cellId) }
            ?: seedableCell?.let { Seed(it.first.cellId, it.second.id) }
            ?: Wait()

        action.play(me.potentialSun.toString())
    }

}
