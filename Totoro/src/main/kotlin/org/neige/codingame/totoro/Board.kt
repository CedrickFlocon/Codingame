package org.neige.codingame.totoro

class Board(
    private val cells: List<Cell>
) {

    companion object {
        const val MAX_DIRECTION = 6
    }

    init {
        cells.flatMap { cell ->
            cell.neighborsId.map { neighborId -> cell to neighborId }
        }.forEach { (cell, neighborsId) ->
            cell.neighbors.add(cells.find { it.id == neighborsId })
        }
    }

    var trees = emptyList<Tree>()
        private set
    var myTrees = emptyList<Tree>()
        private set
    var opponentTrees = emptyList<Tree>()
        private set
    var sun = Sun(0)
        private set

    fun getNeighbors(cell: Cell, distance: Int): List<Cell> {
        val neighbors = cell.neighbors.filterNotNull()

        return (neighbors +
                if (distance > 1) neighbors.flatMap { getNeighbors(it, distance - 1) } else emptyList())
            .distinctBy { it.id }
            .filter { it.id != cell.id }

    }

    fun nextTurn(trees: List<Tree>, day: Int) {
        this.trees = trees
        this.sun.day = day

        cells.forEach {
            it.tree = null
            it.tomorrowShadowSize = 0
        }
        trees.forEach { tree ->
            val cell = cells.find { it.id == tree.cellId }!!
            cell.tree = tree
            tree.cell = cell
        }
        myTrees = trees.filter { it.isMine }
        opponentTrees = trees.filter { !it.isMine }

        cells
            .filter { it.neighbors[sun.tomorrowShadowDirection] == null }
            .forEach { borderCell ->
                var originalCell: Cell? = borderCell

                do {
                    originalCell?.let { cell ->
                        cell.tree?.let { tree ->
                            var shadowCell = cell
                            for (i in tree.size downTo 1) {
                                shadowCell = shadowCell.neighbors[sun.tomorrowSunDirection] ?: break
                                shadowCell.tomorrowShadowSize = maxOf(tree.size, shadowCell.tomorrowShadowSize)
                            }
                        }
                        originalCell = originalCell?.neighbors?.get(sun.tomorrowSunDirection)
                    }
                } while (originalCell != null)
            }

        debug()
    }

    fun debug() {}

}
