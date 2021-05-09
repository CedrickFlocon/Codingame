package org.neige.codingame.totoro

data class Cell(
    val id: Int,
    val richness: Int,
    val neighborsId: List<Int?>
) {
    val richnessScoreBonus: Int
        get() = when (richness) {
            1 -> 0
            2 -> 2
            3 -> 4
            else -> throw IllegalArgumentException()
        }

    val neighbors = mutableListOf<Cell?>()

    var tree: Tree? = null

    val tomorrowSpookyBy: MutableList<Tree> = mutableListOf()

    override fun toString(): String {
        return "Cell[$id] SpookyBy : ${tomorrowSpookyBy.joinToString { "Tree ${it.cellId}" }}"
    }
}
