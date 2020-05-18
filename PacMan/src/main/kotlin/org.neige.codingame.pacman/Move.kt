package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate

sealed class Command(val pac: Pac, protected val message: String? = null)

class Move(pac: Pac, val path: List<Coordinate>, message: String? = null) : Command(pac, message) {

    val destination
        get() = if (path.size >= 2) path[1] else path.first()

    override fun toString() = "MOVE ${pac.id} ${destination.x} ${destination.y} ${message ?: ""}"

}

class Speed(pac: Pac, message: String? = null) : Command(pac, message) {

    override fun toString() = "SPEED ${pac.id} ${message ?: ""}"

}

class Switch(pac: Pac, private val type: Pac.Type, message: String? = null) : Command(pac, message) {

    override fun toString() = "SWITCH ${pac.id} ${type.name} ${message ?: ""}"

}
