package org.neige.codingame.codersstrikeback

import org.neige.codingame.codersstrikeback.pod.Ally
import org.neige.codingame.codersstrikeback.pod.Pod
import java.util.*

class Game(private val input: Scanner,
           private val laps: Int, private val checkpoints: List<Checkpoint>) {

    var pods = emptyList<Ally>()
        private set
    var opponents = emptyList<Pod>()
        private set

    fun init(pods: List<Ally>, opponent: List<Pod>) {
        this.pods = pods
        this.opponents = opponent
    }

    fun nextStep(): Boolean {
        pods.forEach { readInput(it) }
        opponents.forEach { readInput(it) }

        return true
    }

    private fun readInput(pod: Pod) {
        pod.coordinate = Coordinate(input.nextDouble(), input.nextDouble())
        pod.speed = Scalar(input.nextDouble(), input.nextDouble())
        pod.angle = input.nextInt()
        pod.nextCheckpoint = checkpoints[input.nextInt()]
    }

    fun play(): List<Move> {
        return pods.map { it.move() }
    }

    /**
     * Find the next checkpoint
     */
    fun nextCheckpoint(checkpoint: Checkpoint): Checkpoint {
        val checkpointIndex = checkpoints.indexOf(checkpoint)
        return if (checkpointIndex == checkpoints.size - 1) {
            checkpoints[0]
        } else {
            checkpoints[checkpointIndex + 1]
        }
    }

    /**
     * Find the previous checkpoint
     */
    fun previousCheckpoint(checkpoint: Checkpoint): Checkpoint {
        val checkpointIndex = checkpoints.indexOf(checkpoint)
        return if (checkpointIndex == 0) {
            checkpoints[checkpoints.size - 1]
        } else {
            checkpoints[checkpointIndex - 1]
        }
    }

    /**
     * Check if the farthest checkpoint
     */
    fun isFarthestCheckpoint(checkpoint: Checkpoint): Boolean {
        val distanceBetweenCurrentCheckpoint = checkpoint.coordinate.distanceFrom(previousCheckpoint(checkpoint).coordinate)
        return checkpoints.none { it.coordinate.distanceFrom(nextCheckpoint(it).coordinate) > distanceBetweenCurrentCheckpoint }
    }

}