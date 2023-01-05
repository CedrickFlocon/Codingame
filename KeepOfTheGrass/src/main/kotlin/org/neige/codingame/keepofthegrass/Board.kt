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
    val center = width / 2 to height / 2

    //Compute
    val zones = mutableListOf<Zone>()
    var fields = listOf<List<Zone>>()
    var scrapAmount = 0
    var maxPoint = 0

    override fun reset() {
        zones.clear()
        fields = emptyList()
        scrapAmount = 0
        maxPoint = 0
    }

    fun tilesInRange(tile: Tile): List<Tile> {
        return listOfNotNull(
            grid.getOrNull(tile.x - 1)?.get(tile.y),
            grid.getOrNull(tile.x + 1)?.get(tile.y),
            grid[tile.x].getOrNull(tile.y - 1),
            grid[tile.x].getOrNull(tile.y + 1)
        ).filter { !it.grass }
    }

    fun recycled(recyclers: List<Tile>): List<Tile> {
        return recyclers
            .flatMap { recycler -> tilesInRange(recycler).filter { recycler.scrapAmount >= it.scrapAmount } }
    }

    override fun compute() {
        tiles
            .filter { !it.grass }
            .map { tile -> tile to tilesInRange(tile) }
            .forEach { (tile, tilesInRange) ->
                tile.recyclingPotential += tile.scrapAmount + tilesInRange.sumOf { min(tile.scrapAmount, it.scrapAmount) }

                if (tile.recycler) tile.pointPotential = false
                tilesInRange
                    .filter { tile.recycler && tile.scrapAmount >= it.scrapAmount }
                    .forEach { it.pointPotential = false }
            }
        maxPoint = tiles.count { it.pointPotential }

        //Region Zone
        val zonesAndLinks = tiles
            .asSequence()
            .filter { it.free }
            .filter { tile -> zones.none { it.tiles.any { it == tile } } }
            .map { origin ->
                val tiles = mutableSetOf<Tile>()
                val borderLinks = mutableSetOf<Link>()
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
                        .filter { tileInRange -> tileInRange.free && !tiles.contains(tileInRange) && !toInspect.contains(tileInRange) }
                        .forEach { tileInRange ->
                            if (tileInRange.owner == origin.owner) {
                                toInspect.push(tileInRange)
                            } else {
                                borderLinks.add(Link(tileToInspect, tileInRange))
                            }
                        }
                }
                (Zone(
                    tiles = tiles,
                    tileNumber = tilesNumber,
                    scrapAmount = scrapAmount,
                    player = origin.owner,
                    robot = robotNumber,
                )).also { zones.add(it) } to borderLinks
            }
            .toList()

        zonesAndLinks
            .forEach { (zone, borders) ->
                zone.borders = borders
                    .map { link -> zonesAndLinks.first { it.first.tiles.any { it == link.outside } }.first to link }
                    .groupBy { it.first }
                    .mapValues { it.value.map { it.second } }
            }

        fields = zones.map { it.fieldZone }.distinct().toMutableList()
        //EndRegion
    }

    override fun debug() = """
        |Board ($maxPoint) 
        |${zones.joinToString("\n") { "=>${it.debug()}" }}
        """.trimMargin()

}
