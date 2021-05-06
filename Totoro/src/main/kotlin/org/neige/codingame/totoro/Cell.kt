package org.neige.codingame.totoro

data class Cell(
    val id: Int,
    val richness: Int,
    val neighborsId: List<Int?>
) {
    val neighbors = mutableListOf<Cell?>()

    var tree: Tree? = null
    var tomorrowShadowSize = 0

}
