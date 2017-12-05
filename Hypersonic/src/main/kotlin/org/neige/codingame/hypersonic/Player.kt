package org.neige.codingame.hypersonic


data class Player(override val id: Int, override val x: Int, override val y: Int, val bombNumber: Int, val bombRange: Int) : Owner, Located {

    fun play(board: Board): Action {
        val closetedBox = board.getClosestBox(this)

        return if (closetedBox == null) {
            Action(Action.Command.MOVE, Coordinate(0, 0))
        } else if (board.timerToExplode(this) != null) {
            Action(Action.Command.MOVE, board.getClosestSafePlace(this) ?: Coordinate(0, 0), "Safe Place")
        } else {
            if (closetedBox.checkNeighbour(this)) {
                Action(Action.Command.BOMB, closetedBox)
            } else {
                val accessiblePath = board.getAccessiblePath(this)
                var closetedNeighbour: Located? = null
                for (located in accessiblePath) {
                    if (closetedBox.checkNeighbour(located) && (closetedNeighbour == null || located.distanceBetween(this) < closetedNeighbour.distanceBetween(this))) {
                        closetedNeighbour = located
                    }
                }

                val pathTo = board.buildPathTo(this, closetedNeighbour!!)

                val path = pathTo.minBy { it.size }!!
                if (board.timerToExplode(path[1]) == null) {
                    Action(Action.Command.MOVE, path[1], "Move to closest box")
                }
            }

            Action(Action.Command.MOVE, this, "Motionless")
        }
    }

}