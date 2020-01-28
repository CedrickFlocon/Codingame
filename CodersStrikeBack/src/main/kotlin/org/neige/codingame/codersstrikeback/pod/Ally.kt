package org.neige.codingame.codersstrikeback.pod

import org.neige.codingame.codersstrikeback.*


abstract class Ally(game: Game, coordinate: Coordinate, speed: Scalar, angle: Int, nextCheckpoint: Checkpoint) : Pod(game, coordinate, speed, angle, nextCheckpoint) {

    /**
     * Move the pod according to the pod characteristic
     */
    abstract fun move(): Move

}