package org.neige.codingame.ghostinthecell

class Bomb(
        override val id: Int,
        override val diplomacy: Diplomacy,
        val from: Factory,
        val to: Factory?,
        val remainingDistance: Int?
) : Entity {

    var distanceTraveled: Int = 0
        private set

    fun move() {
        distanceTraveled++
    }

    override fun toString(): String {
        return """ Bomb $id $diplomacy 
            | => distanceTraveled:$distanceTraveled
        """.trimMargin()
    }
}
