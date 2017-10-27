package org.neige.codingame.codersstrikeback.pod

import org.neige.codingame.codersstrikeback.*


class Runner(game: Game, coordinate: Coordinate, speed: Scalar, angle: Int, nextCheckpoint: Checkpoint) : Ally(game, coordinate, speed, angle, nextCheckpoint) {

    private var isBoostAvailable = true

    override fun move(): Move {
        return if ((game.opponents + game.pods.find { it != this }!!).any({ willCollide(it) && (speed - it.speed).magnitude() > 200 })) {
            shield(nextCheckpoint)
        } else if (isBoostAvailable && game.isFarthestCheckpoint(nextCheckpoint) && speed.angle(Scalar(coordinate, nextCheckpoint.coordinate)) < 30 && coordinate.distanceFrom(nextCheckpoint.coordinate) > 4000) {
            isBoostAvailable = false
            Move(getBestPath(nextCheckpoint), "BOOST")
        } else {
            if ((1..4).any({ nextCheckpoint.isInCheckpoint(coordinate + (speed * it.toDouble())) })) {
                Move(getBestPath(game.nextCheckpoint(nextCheckpoint)), "0")
            } else {
                Move(getBestPath(nextCheckpoint), "200")
            }
        }
    }
}