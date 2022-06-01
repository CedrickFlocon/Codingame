package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector

data class Spider(
    val id: Int,
    val coordinate: Coordinate,
    val health: Int,
    val trajectory: Vector,
    val nearBase: Boolean,
    val threatFor: Player?
)
