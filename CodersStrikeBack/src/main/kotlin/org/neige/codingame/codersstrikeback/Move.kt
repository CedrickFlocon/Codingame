package org.neige.codingame.codersstrikeback

data class Move(val coordinate: Coordinate, val thrust: String) {

    override fun toString(): String {
        return "${coordinate.x.toInt()} ${coordinate.y.toInt()} $thrust"
    }

}