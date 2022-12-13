package org.neige.codingame.keepofthegrass

import java.util.Stack
import kotlin.math.min

class Board(
    val width: Int,
    val height: Int,
) : Resettable, Computable, Debuggable {

    //Input
    val grid = Array(width) { x -> Array(height) { y -> Tile(x, y) } }
    val tiles = grid.flatten()

    //Compute
    val zones = mutableListOf<Zone>()
    val fields = mutableListOf<List<Zone>>()
    var scrapAmount = 0
    var maxTile = 0

    override fun reset() {
        zones.clear()
        fields.clear()
        scrapAmount = 0
        maxTile = 0
    }

    fun tilesInRange(tile: Tile): List<Tile> {
        return listOfNotNull(
            grid.getOrNull(tile.x - 1)?.get(tile.y),
            grid.getOrNull(tile.x + 1)?.get(tile.y),
            grid[tile.x].getOrNull(tile.y - 1),
            grid[tile.x].getOrNull(tile.y + 1)
        ).filter { !it.grass }
    }

    override fun compute() {
        val zonesAndLinks = tiles
            .asSequence()
            .filter { it.free }
            .filter { tiles -> zones.none { it.tiles.any { it == tiles } } }
            .map { origin ->
                val tiles = mutableSetOf<Tile>()
                val borderTiles = mutableSetOf<Link>()
                var scrapAmount = 0
                var tilesNumber = 0
                var robotNumber = 0

                val toInspect = Stack<Tile>().apply { this.push(origin) }

                while (toInspect.isNotEmpty()) {
                    val tileToInspect = toInspect.pop()
                    val inRange = tilesInRange(tileToInspect)

                    scrapAmount += tileToInspect.scrapAmount
                    tilesNumber++
                    robotNumber += tileToInspect.robot
                    tileToInspect.recyclingPotential = tileToInspect.scrapAmount + inRange.sumOf { min(tileToInspect.scrapAmount, it.scrapAmount) }
                    tiles.add(tileToInspect)

                    inRange
                        .onEach {
                            if (it.recycler && it.scrapAmount >= tileToInspect.scrapAmount) {
                                tileToInspect.willBecomeGrass = true
                            }
                        }
                        .filter { tileInRange -> tileInRange.free && !tiles.contains(tileInRange) && !toInspect.contains(tileInRange) }
                        .forEach { tileInRange ->
                            if (tileInRange.owner == origin.owner) {
                                toInspect.push(tileInRange)
                            } else {
                                borderTiles.add(Link(tileToInspect, tileInRange))
                            }
                        }
                }
                (Zone(
                    tiles = tiles,
                    tileNumber = tilesNumber,
                    scrapAmount = scrapAmount,
                    player = origin.owner,
                    robot = robotNumber,
                )).also { zones.add(it) } to borderTiles
            }
            .toList()

        zonesAndLinks
            .forEach { (zone, borders) ->
                zone.borders = borders
                    .map { link -> zonesAndLinks.first { it.first.tiles.any { it == link.outside } }.first to link }
                    .groupBy { it.first }
                    .mapValues {
                        it.value.map { it.second }
                    }
            }

        fields.addAll(zones.map { it.fieldZone }.distinct().toMutableList())

        maxTile = tiles.count { !it.willBecomeGrass }
    }

    override fun debug() = """
        |Board ($maxTile) 
        |${zones.joinToString("\n") { "=>${it.debug()}" }}
        """.trimMargin()

}
