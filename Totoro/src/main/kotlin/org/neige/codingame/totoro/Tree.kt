package org.neige.codingame.totoro

data class Tree(
    val cellId: Int,
    val size: Int,
    val owner: Player,
    val isDormant: Boolean,
    val nutrients: Int
) {

    lateinit var cell: Cell

    val potentialScore: Int?
        get() = if (size == 3) nutrients + cell.richnessScoreBonus else null

    val tomorrowSunPoint: Int
        get() = if (tomorrowSpooky) 0 else size

    val tomorrowSpooky: Boolean
        get() = cell.tomorrowShadowSize > 0 && cell.tomorrowShadowSize >= size

    val tomorrowSpookySize: Int
        get() = cell.tomorrowShadowSize - size

    override fun toString(): String {
        return "Tree[$cellId] $tomorrowSpooky $tomorrowSunPoint $tomorrowSpookySize $potentialScore"
    }
}
