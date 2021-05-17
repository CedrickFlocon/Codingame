package org.neige.codingame.totoro.state

data class Board(
    val cells: Map<Int, Cell>
) {

    companion object {
        private const val MAX_DIRECTION = 6
        private const val MAX_DISTANCE = 7
    }

    val cellsNeighborsSunDirection = cells.map { (cellId, cell) ->
        cellId to (0 until MAX_DIRECTION).map { sunDirection ->
            sunDirection to getNeighborsInSunDirection(cell, sunDirection, MAX_DISTANCE)
        }.toMap()
    }.toMap()

    private fun getNeighborsInSunDirection(cell: Cell, sunDirection: Int, distance: Int, distanceFromOrigin: Int = 1): List<Pair<Cell, Int>> {
        val neighbors = cells[cell.neighborsId[sunDirection]] ?: return emptyList()

        return listOf(neighbors to distanceFromOrigin) +
                if (distance > 1) {
                    getNeighborsInSunDirection(neighbors, sunDirection, distance - 1, distanceFromOrigin + 1)
                } else {
                    emptyList()
                }
    }

}
