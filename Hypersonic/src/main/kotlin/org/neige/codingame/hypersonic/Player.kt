package org.neige.codingame.hypersonic


data class Player(override val id: Int, override val x: Int, override val y: Int, val bombNumber: Int, val bombRange: Int) : Owner, Located {

    fun play(board: Board): Action {
        val closetedBox = board.getClosestBox(this)
        return if (closetedBox == null) {
            Action("MOVE", 0, 0)
        } else {
            val type = if (closetedBox.checkNeighbour(this)) {
                "BOMB"
            } else {
                "MOVE"
            }
            return Action(type, closetedBox.x, closetedBox.y)
        }
    }
}