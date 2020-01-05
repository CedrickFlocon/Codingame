package org.neige.codingame.ghostinthecell

data class Bomb(
        override val id: Int,
        override val diplomacy: Diplomacy,
        val from: Factory,
        val to: Factory?,
        val remainingDistance: Int?
) : Entity