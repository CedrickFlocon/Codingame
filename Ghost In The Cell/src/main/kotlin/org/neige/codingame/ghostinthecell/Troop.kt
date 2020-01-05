package org.neige.codingame.ghostinthecell

data class Troop(
        override val id: Int,
        override val diplomacy: Diplomacy,
        val from: Factory,
        val to: Factory,
        val cyborgsNumber: Int,
        val remainingDistance: Int
) : Entity