package org.neige.codingame.totoro

class Day(
    var day: Int
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

    fun sunDirectionIn(day: Int = 0) = (this.day + day) % Board.MAX_DIRECTION

    fun oppositeSunDirectionIn(day: Int = 0) = (this.day + day + Board.MAX_DIRECTION / 2) % Board.MAX_DIRECTION

    override fun toString(): String {
        return "$day $dayPercentage\n" +
                "$countDown $countDownPercentage"
    }
}
