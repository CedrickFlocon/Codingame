package org.neige.codingame.keepofthegrass

import java.util.Stack
import kotlin.math.min

class Board(
    val width: Int,
    val height: Int,
) {

    val grid = Array(width) { x -> Array(height) { y -> Tile(x, y) } }
    val tiles = grid.flatten()
    val zones = mutableListOf<Zone>()

    private fun Tile.inRange(): List<Tile> {
        return listOfNotNull(
            grid.getOrNull(x - 1)?.get(y),
            grid.getOrNull(x + 1)?.get(y),
            grid[x].getOrNull(y - 1),
            grid[x].getOrNull(y + 1)
        )
    }

    fun tilesInRange(tile: Tile): List<Tile> {
        return tile.inRange()
    }

    private fun Tile.field(): List<Tile> {
        val tiles = mutableListOf<Tile>()
        val toInspect = Stack<Tile>()
        toInspect.push(this)

        while (toInspect.isNotEmpty()) {
            val tileToInspect = toInspect.pop()
            tiles.add(tileToInspect)

            tileToInspect.inRange()
                .filter { tile -> tile.scrapAmount > 0 && !tile.recycler && tiles.none { it.x == tile.x && it.y == tile.y } }
                .forEach { toInspect.push(it) }
        }

        return tiles
    }

    fun compute() {
        tiles.forEach { origin ->
            origin.recyclingPotential = origin.scrapAmount + origin.inRange().sumOf { min(origin.scrapAmount, it.scrapAmount) }
        }

        zones.clear()
        tiles
            .asSequence()
            .filter { it.scrapAmount > 0 }
            .filter { tiles -> zones.none { it.tiles.any { it == tiles } } }
            .forEach { origin ->
                val tiles = origin.field()
                zones.add(
                    Zone(
                        tiles = tiles,
                        scrapAmount = tiles.sumOf { it.scrapAmount },
                        neutralTile = tiles.count { it.owner == null },
                        myTile = tiles.count { it.owner == Owner.ME },
                        myUnit = tiles.filter { it.owner == Owner.ME }.sumOf { it.units },
                        myRecycler = tiles.filter { it.owner == Owner.ME }.count { it.recycler },
                        opponentTile = tiles.count { it.owner == Owner.OPPONENT },
                        opponentUnit = tiles.filter { it.owner == Owner.OPPONENT }.sumOf { it.units },
                        opponentRecycler = tiles.filter { it.owner == Owner.OPPONENT }.count { it.recycler },
                    )
                )
            }
    }

}
