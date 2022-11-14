package org.neige.codingame.codersstrikeback.pod

import org.neige.codingame.codersstrikeback.*


class Hunter(game: Game, coordinate: Coordinate, speed: Scalar, angle: Int, nextCheckpoint: Checkpoint) : Ally(game, coordinate, speed, angle, nextCheckpoint) {

    override fun move(): Move {
        return if (game.opponents.any({ willCollide(it) && (speed - it.speed).magnitude() > 200 })) {
            shield(nextCheckpoint)
        } else {
            val opponent = game.opponents.maxOrNull()!!
            if (opponent.coordinate.distanceFrom(opponent.nextCheckpoint.coordinate) < coordinate.distanceFrom(opponent.nextCheckpoint.coordinate)) {
                val opponentCheckpoint = game.nextCheckpoint(opponent.nextCheckpoint)
                when {
                    coordinate.distanceFrom(opponentCheckpoint.coordinate) > 5000 -> Move(getBestPath(game.nextCheckpoint(opponent.nextCheckpoint)), "200")
                    coordinate.distanceFrom(opponentCheckpoint.coordinate) > 2500 -> Move(getBestPath(game.nextCheckpoint(opponent.nextCheckpoint)), "100")
                    else -> Move(opponent.coordinate, "0")
                }
            } else {
                Move(opponent.coordinate + (opponent.speed - speed) * 3.0, "200")
            }
        }
    }

}