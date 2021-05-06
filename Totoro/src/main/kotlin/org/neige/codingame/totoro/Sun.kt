package org.neige.codingame.totoro

class Sun(
    var day: Int
) {

    val sunDirection
        get() = day % Board.MAX_DIRECTION

    val tomorrowSunDirection
        get() = (sunDirection + 1) % Board.MAX_DIRECTION

    val shadowDirection
        get() = (sunDirection + Board.MAX_DIRECTION / 2) % Board.MAX_DIRECTION

    val tomorrowShadowDirection
        get() = (tomorrowSunDirection + Board.MAX_DIRECTION / 2) % Board.MAX_DIRECTION

}