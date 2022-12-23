package org.neige.codingame.keepofthegrass

import kotlin.math.abs

class Tile(
    val x: Int,
    val y: Int,
) : Resettable, Debuggable {

    //Input
    var scrapAmount: Int = 0
    var owner: Player? = null
    var robot: Int = 0
    var recycler: Boolean = false
    var canBuild: Boolean = false
    var canSpawn: Boolean = false
    var inRangeOfRecycler: Boolean = false

    //Compute
    //var zoneDensity: Map<Player?, Int> = emptyMap()
    //var robotDensity: Map<Player, Int> = emptyMap()

    var pointPotential: Boolean = true
        get() = field && !grass

    var recyclingPotential: Int = 0

    val grass: Boolean
        get() = scrapAmount == 0

    val nextTurnGrass: Boolean
        get() = scrapAmount == 1 && inRangeOfRecycler

    val free: Boolean
        get() = !recycler && !grass

    val empty: Boolean
        get() = free && robot == 0

    fun distanceTo(tile: Tile) = abs(this.x - tile.x) + abs(this.y - tile.y)

    override fun reset() {
        pointPotential = true
        recyclingPotential = 0
    }

    override fun toString() = "$x:$y"
    override fun debug() = "$x:$y $recyclingPotential"
}
