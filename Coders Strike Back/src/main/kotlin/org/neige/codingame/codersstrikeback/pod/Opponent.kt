package org.neige.codingame.codersstrikeback.pod

import org.neige.codingame.codersstrikeback.Checkpoint
import org.neige.codingame.codersstrikeback.Coordinate
import org.neige.codingame.codersstrikeback.Game
import org.neige.codingame.codersstrikeback.Scalar


class Opponent(game: Game, coordinate: Coordinate, speed: Scalar, angle: Int, nextCheckpoint: Checkpoint) : Pod(game, coordinate, speed, angle, nextCheckpoint)