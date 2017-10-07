package org.neige.codingame.codersstrikeback

data class Move(private val coordinate: Coordinate, var thrust: String? = null) {

    fun move(): String {
        if (thrust == null) {
            throw IllegalStateException("You must init thrust before moving")
        }
        return "${coordinate.x.toInt()} ${coordinate.y.toInt()} $thrust"
    }

}