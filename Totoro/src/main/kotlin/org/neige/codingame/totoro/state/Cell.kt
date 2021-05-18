package org.neige.codingame.totoro.state

data class Cell(
    val id: Int,
    val richness: Int,
    val neighborsId: List<Int?>
) {

    companion object {
        private val RICHNESS_SCORE_BONUS = mapOf(
            1 to 0,
            2 to 2,
            3 to 4
        )
    }

    val richnessScore: Int
        get() = RICHNESS_SCORE_BONUS[richness] ?: throw IllegalArgumentException()

}
