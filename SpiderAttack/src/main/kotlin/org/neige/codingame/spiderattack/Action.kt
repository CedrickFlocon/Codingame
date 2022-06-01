package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate

object Action {

    fun halt() {
        println("WAIT")
    }

    fun move(coordinate: Coordinate) {
        println("MOVE ${coordinate.x} ${coordinate.y}")
    }

}