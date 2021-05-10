package org.neige.codingame.totoro

class Day(
    var day: Int
) {

    companion object {
        const val MAX_DAY = 23
    }

    val dayCountDown: Int
        get() = MAX_DAY - day

    fun sunDirectionIn(day: Int = 0) = (this.day + day) % Board.MAX_DIRECTION

    fun oppositeSunDirectionIn(day: Int = 0) = (this.day + day + Board.MAX_DIRECTION / 2) % Board.MAX_DIRECTION

}
