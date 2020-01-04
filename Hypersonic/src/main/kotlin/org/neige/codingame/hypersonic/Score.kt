package org.neige.codingame.hypersonic

data class Score(
        val located: Located,
        val shortestPath: List<Located>,
        val distance: Int,
        val isSafePath: Boolean,
        val isDeadEnd: Boolean,
        val isSuicide: Boolean,
        val destroyableBoxNumber: Int,
        val timerToExplode: Int?,
        val player: Player
) : Located {

    override val x: Int
        get() = located.x

    override val y: Int
        get() = located.y

    fun score(): Float {
        val safety = (timerToExplode ?: 10)

        return if (player.bombNumber > 0) {
            (destroyableBoxNumber * 5 - distance + safety).toFloat()
        } else {
            (destroyableBoxNumber * 5 + safety).toFloat()
        }
    }

    override fun toString(): String {
        return "[$x;$y] isSafePath:$isSafePath isDeadEnd:$isDeadEnd isSuicide:$isSuicide"
    }
}