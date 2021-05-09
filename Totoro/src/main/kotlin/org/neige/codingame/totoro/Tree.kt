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
        get() = size.takeIf { tomorrowSpookyBy.isEmpty() } ?: 0

    val tomorrowSpookyBy: List<Tree>
        get() = cell.tomorrowSpookyBy.filter { it.size >= size }

    val tomorrowSpookySize: Int?
        get() = cell.tomorrowSpookyBy.maxBy { it.size }?.size?.minus(size)

    override fun toString(): String {
        return "Tree[$cellId] ${tomorrowSpookyBy.joinToString { "Tree ${it.cellId}" }} $tomorrowSpookySize $tomorrowSunPoint $potentialScore"
    }
}
