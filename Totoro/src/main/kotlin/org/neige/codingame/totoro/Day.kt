package org.neige.codingame.totoro

class Day(
    var day: Int
) {

    companion object {
        const val MAX_DAY = 23
    }

    val sunDirection
        get() = day % Board.MAX_DIRECTION

    val tomorrowSunDirection
        get() = (sunDirection + 1) % Board.MAX_DIRECTION

    val shadowDirection
        get() = (sunDirection + Board.MAX_DIRECTION / 2) % Board.MAX_DIRECTION

    val tomorrowShadowDirection
        get() = (tomorrowSunDirection + Board.MAX_DIRECTION / 2) % Board.MAX_DIRECTION

    val dayCountDown: Int
        get() = MAX_DAY - day

}