package org.neige.codingame.hypersonic


data class Player(override val id: Int, override val x: Int, override val y: Int, val bombNumber: Int, val bombRange: Int) : Owner, Located {

    fun play(board: Board): Action {
        val closetedBox = board.getClosestBox(this)

        val playerExplosionTimer = board.timerToExplode(this)
        return if (playerExplosionTimer != null) { // we can walk into explosion
            val closestSafePlace = board.getClosestSafePlace(this)
            if (closestSafePlace != null) {
                Action(Action.Command.MOVE, closestSafePlace, "Safe Place")
            } else {
                val higherTimerPlace = board.getAccessiblePath(this).maxBy { board.timerToExplode(it) ?: 0 }
                if (higherTimerPlace != null && playerExplosionTimer == board.timerToExplode(higherTimerPlace)) {
                    Action(Action.Command.MOVE, this, "Don't move for now")
                } else if (higherTimerPlace != null) {
                    Action(Action.Command.MOVE, higherTimerPlace, "We have time")
                } else {
                    Action(Action.Command.MOVE, Coordinate(0, 0), "No safe place")
                }
            }
        } else if (closetedBox == null) {
            Action(Action.Command.MOVE, Coordinate(0, 0), "No box accessible")
        } else {
            if (closetedBox.checkNeighbour(this)) {
                Action(Action.Command.BOMB, closetedBox, "let's destroy this box")
            } else {
                val accessiblePath = board.getAccessiblePath(this)
                var closetedNeighbour: Located? = null
                for (located in accessiblePath) {
                    if (closetedBox.checkNeighbour(located) && (closetedNeighbour == null || located.distanceBetween(this) < closetedNeighbour.distanceBetween(this))) {
                        closetedNeighbour = located
                    }
                }

                val countElementType = board.countAccessibleElementType(this, Floor::class)
                Log.debug("Accessible Floor number : $countElementType")
                if (countElementType < 50) {
                    val pathTo = board.buildPathTo(this, closetedNeighbour!!)

                    val path = pathTo.minBy { it.size }!!

                    if (board.timerToExplode(path[1]) == null) { //could go to dead end
                        Action(Action.Command.MOVE, path[1], "Move to closest box by path")
                    } else {
                        Action(Action.Command.MOVE, this, "Motionless But destination ${path[1]}")
                    }
                } else {
                    Action(Action.Command.MOVE, closetedBox, "Move to closest box")
                }
            }
        }
    }

}