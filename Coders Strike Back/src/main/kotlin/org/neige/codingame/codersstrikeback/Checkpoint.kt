package org.neige.codingame.codersstrikeback

data class Checkpoint(val coordinate: Coordinate) {

    constructor(x: Double, y:Double):this(Coordinate(x, y))

    private val RADIUS_SIZE = 600

    /**
     * Check if a pod is in the checkpoint
     */
    fun isInCheckpoint(coordinate: Coordinate): Boolean {
        return this.coordinate.distanceFrom(coordinate) < RADIUS_SIZE
    }

}