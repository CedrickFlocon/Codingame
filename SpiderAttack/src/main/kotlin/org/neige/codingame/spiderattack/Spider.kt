package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector

data class Spider(
    override val id: Int,
    override val coordinate: Coordinate,
    val health: Int,
    val trajectory: Vector,
    val nearBase: Boolean,
    val threatFor: Player?
) : Entity {

    companion object {
        const val SPEED = 400
        const val TARGET_RANGE = 5000
        const val ATTACK_RANGE = 300
    }

    fun targetRadiusDistance(base: Base) = base.coordinate.distanceFrom(coordinate) - TARGET_RANGE

    private fun debug() {
        System.err.println(
            """
            Spider : $id
            B->Me ${targetRadiusDistance(Board.playerBase(Player.ME))} B->Op Me  B->Op ${targetRadiusDistance(Board.opponentBase(Player.ME))}
            """.trimIndent()
        )
    }

}
