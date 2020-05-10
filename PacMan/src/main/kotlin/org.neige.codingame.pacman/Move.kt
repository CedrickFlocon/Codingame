package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate

sealed class Command(protected val pacId: Int, protected val message: String? = null)

class Move(pacId: Int, private val coordinate: Coordinate, message: String? = null) : Command(pacId, message) {

    override fun toString() = "MOVE $pacId ${coordinate.x} ${coordinate.y} ${message ?: ""}"

}

class Speed(pacId: Int, message: String? = null) : Command(pacId, message) {

    override fun toString() = "SPEED $pacId ${message ?: ""}"

}

class Switch(pacId: Int, private val type: Pac.Type, message: String? = null) : Command(pacId, message) {

    override fun toString() = "SWITCH $pacId ${type.name} ${message ?: ""}"

}
