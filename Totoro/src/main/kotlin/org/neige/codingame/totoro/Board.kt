package org.neige.codingame.totoro

import org.neige.codingame.util.Log

class Board(
    private val cells: List<Cell>
) {

    companion object {
        const val MAX_DIRECTION = 6
        const val MAX_NUTRIENTS = 20
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

    var nutrients = 0
    val nutrientsPercentage: Double
        get() = nutrients.toDouble() / MAX_NUTRIENTS

    val day = Day(0)

    fun getNeighbors(cell: Cell, distance: Int, distanceFromOrigin: Int = 1): List<Pair<Cell, Int>> {
        val neighbors = cell.neighbors.filterNotNull().map { it to distanceFromOrigin }

        return (neighbors +
                if (distance > 1) neighbors.flatMap { getNeighbors(it.first, distance - 1, distanceFromOrigin + 1) } else emptyList())
            .distinctBy { it.first.id }
            .filter { it.first.id != cell.id }
    }

    fun getNeighborsInSunDirection(cell: Cell, sunDirection: Int, distance: Int, distanceFromOrigin: Int = 1): List<Pair<Cell, Int>> {
        val neighbors = cell.neighbors[sunDirection] ?: return emptyList()

        return listOf(neighbors to distance) +
                if (distance > 1) {
                    getNeighborsInSunDirection(neighbors, sunDirection, distance - 1, distanceFromOrigin + 1)
                } else {
                    emptyList()
                }
    }

    fun nextTurn(trees: List<Tree>, day: Int) {
        this.trees = trees
        this.day.day = day

        cells.forEach { it.tree = null }
        trees.forEach { tree ->
            val cell = cells.find { it.id == tree.cellId }!!
            cell.tree = tree
            tree.cell = cell
        }

        buildShadow()

        debug()
    }

    fun play(action: Action) {
        when (action) {
            is Wait -> return
            is Complete -> nextTurn(trees.filter { it != action.tree }, day.day).also { nutrients -= 1 }
            is Seed -> nextTurn(trees + Tree(action.cell.id, 0, action.player, true, nutrients), day.day)
            is Grow -> nextTurn(trees.filter { it != action.tree } + action.tree.copy(size = action.expectedTreeSize, isDormant = true), day.day)
        }.also { action.player.sunPoints -= action.sunCost }
    }

    fun buildShadow() {
        cells.forEach { it.spookyBy.forEach { it.value.clear() } }

        (1..day.countDown).forEach { nextDay ->
            cells
                .filter { it.neighbors[day.oppositeSunDirectionIn(nextDay)] == null }
                .forEach { borderCell ->
                    var originalCell: Cell? = borderCell

                    do {
                        originalCell?.let { cell ->
                            cell.tree?.let { tree ->
                                var shadowCell = cell
                                for (i in tree.size downTo 1) {
                                    shadowCell = shadowCell.neighbors[day.sunDirectionIn(nextDay)] ?: break
                                    shadowCell.spookyBy[nextDay]!!.add(tree)
                                }
                            }
                            originalCell = originalCell?.neighbors?.get(day.sunDirectionIn(nextDay))
                        }
                    } while (originalCell != null)
                }
        }
    }

    private fun debug() {
        //Log.debug(day)
    }

}
