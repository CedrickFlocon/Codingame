package org.neige.codingame.keepofthegrass

class Tile(
    val x: Int,
    val y: Int,
    var scrapAmount: Int = 0,
    var recyclingPotential: Int = 0,
    var owner: Owner? = null,
    var units: Int = 0,
    var recycler: Boolean = false,
    var canBuild: Boolean = false,
    var canSpawn: Boolean = false,
    var inRangeOfRecycler: Boolean = false
) {

    val nextTurnGrass: Boolean = scrapAmount == 1 && inRangeOfRecycler

    val free: Boolean = !recycler && scrapAmount > 0

    override fun toString(): String {
        return "[$x][$y]"
    }
}