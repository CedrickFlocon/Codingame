package org.neige.codingame.codersstrikeback


class Pod {
    private val SIZE: Int = 400

    private var speed = Vector(0.0, 0.0)
    var isInit = false
    var position = Coordinate(0.0, 0.0)
        set(value) {
            if (isInit) {
                speed = Vector(value.x - field.x, value.y - field.y)
            }
            isInit = true
            field = value
        }
    private var isBoostAvailable = true

    fun move(checkpoint: Coordinate, isFarthestCheckpoint: Boolean, angle: Int, opponentPod: Pod): Move {
        val move = Move(checkpoint - speed)

        if (willCollide(opponentPod) && (speed - opponentPod.speed).magnitude() > 400) {
            move.thrust = "SHIELD"
        } else if (angle == 0 && isBoostAvailable && isFarthestCheckpoint) {
            move.thrust = "BOOST"
            isBoostAvailable = false
        } else if (position.distanceFrom(checkpoint) < 1500 && speed.magnitude() > 200) {
            move.thrust = "40"
        } else {
            move.thrust = "100"
        }

        return move
    }

    fun willCollide(opponentPod: Pod): Boolean {
        var opponentPosition = opponentPod.position
        var myPosition = position
        var proportion: Double
        for (i in 0..400) {
            proportion = (i.toDouble() / 400)
            if (opponentPosition.distanceFrom(myPosition) < SIZE * 2) {
                return true
            }

            opponentPosition = Coordinate(proportion * opponentPod.speed.x + opponentPod.position.x, proportion * opponentPod.speed.y + opponentPod.position.y)
            myPosition = Coordinate(proportion * speed.x + position.x, proportion * speed.y + position.y)
        }

        return false
    }

    override fun toString(): String {
        return "Position $position\nSpeed : $speed"
    }
}