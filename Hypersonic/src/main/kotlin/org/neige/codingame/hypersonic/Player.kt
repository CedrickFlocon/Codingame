package org.neige.codingame.hypersonic

import org.neige.codingame.util.Log


data class Player(override val id: Int, override val x: Int, override val y: Int, val bombNumber: Int, val bombRange: Int) : Owner, Located {

    fun play(board: Board): Action {
        val scores = buildScore(board)

        scores.forEach { Log.debug(it.toString()) }

        val safestPlace = scores.sortedByDescending { it.timerToExplode ?: 10 }.firstOrNull { it.isSafePath && !it.isDeadEnd }
                ?: scores.filter { !it.isSafePath && !it.isDeadEnd }.maxBy { it.timerToExplode ?: 10 }
                ?: scores.maxBy { it.timerToExplode ?: 10 }!!

        return if (scores.none { it.destroyableBoxNumber > 0 }) {
            Action(Action.Command.MOVE, safestPlace, "I will kill you")
        } else {
            val bestScore = scores
                    .filter { it.isSafePath && !it.isSuicide && !it.isDeadEnd && board.getGridElement(it.located) !is Bomb }
                    .maxBy { it.score() }

            Log.debug("Best score $bestScore")

            if (bestScore?.sameLocated(this) == true && bestScore.destroyableBoxNumber > 0) {
                Action(Action.Command.BOMB, bestScore, "let's destroy this box")
            } else if (bestScore != null) {
                Action(Action.Command.MOVE, if (bestScore.shortestPath.isEmpty()) this else bestScore.shortestPath[0], "Best Score ${bestScore.located.x};${bestScore.located.y}")
            } else {
                Action(Action.Command.MOVE, this, "Sorry no safe place")
            }
        }
    }

    private fun buildScore(board: Board): List<Score> {
        val scores = mutableListOf<Score>()
        for (located in board.getAccessiblePath(this).sortedBy { it.distanceBetween(this) }.take(40)) {
            val shortestPath = board.shortestPath(this, located)
            val distance = shortestPath.size

            var isSafePath = true
            if (distance == 0) {
                val pathTimer = board.timerToExplode(this)
                isSafePath = pathTimer == null || pathTimer > 3
            } else {
                for ((index, pathCoordinate) in shortestPath.withIndex()) {
                    val pathTimer = board.timerToExplode(pathCoordinate)
                    if (pathTimer == null) {
                        isSafePath = true
                        break
                    } else if (pathTimer - 2 == index) {
                        isSafePath = false
                        break
                    }
                }
            }

            val destroyableBoxNumber = board.destroyableBoxNumber(located, bombRange - 1)
            val timerToExplode = board.timerToExplode(located)

            val isDeadEnd = board.isDeadEnd(located)
            val isSuicide = board.isSuicide(located, Bomb(-1, located.x, located.y, 10, bombRange))

            scores += Score(
                    Coordinate(located.x, located.y),
                    shortestPath,
                    distance,
                    isSafePath,
                    isDeadEnd,
                    isSuicide,
                    destroyableBoxNumber,
                    timerToExplode,
                    this
            )
        }

        return scores
    }
}