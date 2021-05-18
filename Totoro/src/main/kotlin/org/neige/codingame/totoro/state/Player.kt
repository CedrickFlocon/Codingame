package org.neige.codingame.totoro.state

data class Player(
    val who: Who,
    val sunPoints: Int,
    val score: Int,
    val isWaiting: Boolean
) {

    enum class Who {
        RED,
        BLUE
    }

}
