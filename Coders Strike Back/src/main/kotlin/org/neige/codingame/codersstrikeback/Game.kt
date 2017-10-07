package org.neige.codingame.codersstrikeback

import java.util.*

class Game(private val myPod: Pod, private val opponentPod: Pod, private val input: Scanner) {

    var nextCheckpoint = Coordinate(0.0, 0.0)
        private set
    var currentCheckpoint: Coordinate? = null
        private set
    var allCheckpoint = emptyList<Coordinate>()
        private set
    var lap = 1
        private set

    fun nextStep() {
        myPod.position = Coordinate(input.nextDouble(), input.nextDouble())
        val nextCheckpoint = Coordinate(input.nextDouble(), input.nextDouble())
        val distance = input.nextInt()
        val angle = input.nextInt()
        opponentPod.position = Coordinate(input.nextDouble(), input.nextDouble())

        if (this.nextCheckpoint != nextCheckpoint && allCheckpoint.isNotEmpty()) {
            currentCheckpoint = this.nextCheckpoint
            if (allCheckpoint[0] == nextCheckpoint) {
                lap++
            }
        }

        if (!allCheckpoint.contains(nextCheckpoint)) {
            allCheckpoint += nextCheckpoint
        }

        this.nextCheckpoint = nextCheckpoint

        println(myPod.move(nextCheckpoint, isFarthestCheckpoint(), angle, opponentPod).move())
    }

    private fun isFarthestCheckpoint(): Boolean {
        if (lap < 2) return false
        val currentCheckpoint = this.currentCheckpoint ?: return false
        val distanceBetweenCurrentCheckpoint = currentCheckpoint.distanceFrom(getNextCheckpoint(currentCheckpoint))

        return allCheckpoint.none { distanceBetweenCurrentCheckpoint < it.distanceFrom(getNextCheckpoint(it)) }
    }

    private fun getNextCheckpoint(coordinate: Coordinate): Coordinate {
        return if (allCheckpoint.indexOf(coordinate) == allCheckpoint.size - 1) allCheckpoint[0] else allCheckpoint[allCheckpoint.indexOf(coordinate) + 1]
    }
}