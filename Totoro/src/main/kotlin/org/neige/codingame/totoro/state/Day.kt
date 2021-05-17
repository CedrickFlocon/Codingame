package org.neige.codingame.totoro.state

import org.neige.codingame.totoro.Game

data class Day(
    val day: Int
) {

    companion object {
        const val MAX_DAY = 23
    }

    val countDown: Int
        get() = MAX_DAY - day
    val countDownPercentage: Double
        get() = (countDown).toDouble() / (MAX_DAY)

    val dayPercentage: Double
        get() = (day).toDouble() / (MAX_DAY)

    fun sunDirectionIn(day: Int = 0) = (this.day + day) % Game.MAX_DIRECTION

    fun oppositeSunDirectionIn(day: Int = 0) = (this.day + day + Game.MAX_DIRECTION / 2) % Game.MAX_DIRECTION

}
