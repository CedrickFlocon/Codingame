package org.neige.codingame.codersstrikeback.pod

import org.neige.codingame.codersstrikeback.*


abstract class Pod(val game: Game, open var coordinate: Coordinate, open var speed: Scalar, open var angle: Int, nextCheckpoint: Checkpoint) : Comparable<Pod> {

    protected val SHIELD_RADIUS_SIZE = 400
    protected val MAX_ROTATION_DEGREES = 18
    protected val FRICTION = 0.85

    open var nextCheckpoint = nextCheckpoint
        set(value) {
            if (value != field) {
                checkpointReached++
                field = value
            }
        }

    private var checkpointReached = 0

    /**
     * Shield the pod and change angle
     */
    fun shield(checkpoint: Checkpoint): Move {
        return Move(getBestPath(checkpoint), "SHIELD")
    }

    /**
     * return true if the pod will collide otherwise false
     */
    protected fun willCollide(pod: Pod): Boolean {
        var opponentPosition = pod.coordinate
        var myPosition = coordinate

        for (i in 1..400) {
            if (opponentPosition.distanceFrom(myPosition) < SHIELD_RADIUS_SIZE * 2) {
                return true
            }

            opponentPosition += pod.speed / 400.0
            myPosition += speed / 400.0
        }

        return false
    }

    /**
     * Find the best path to reached the coordinate
     */
    fun getBestPath(checkpoint: Checkpoint): Coordinate {
        return checkpoint.coordinate - speed * 3.0
    }

    override fun compareTo(other: Pod): Int {
        val checkpointDifference = checkpointReached - other.checkpointReached
        if (checkpointDifference == 0) {
            return (other.coordinate.distanceFrom(other.nextCheckpoint.coordinate) - coordinate.distanceFrom(nextCheckpoint.coordinate)).toInt()
        }
        return checkpointDifference
    }

    override fun toString(): String {
        return "pod.coordinate = Coordinate(${coordinate.x}, ${coordinate.y})\n" +
                "pod.speed = Scalar(${speed.x}, ${speed.y})\n" +
                "pod.nextCheckpoint = Checkpoint(Coordinate(${nextCheckpoint.coordinate.x},${nextCheckpoint.coordinate.y}))\n" +
                "Angle ${speed.angle(Scalar(coordinate, nextCheckpoint.coordinate))}"
    }
}