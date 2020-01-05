package org.neige.codingame.ghostinthecell

import java.util.*


fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val factoryCount = input.nextInt()
    val linkCount = input.nextInt()
    for (i in 0 until linkCount) {
        val factory1 = input.nextInt()
        val factory2 = input.nextInt()
        val distance = input.nextInt()
    }

    while (true) {
        val entityCount = input.nextInt()

        val factories = mutableListOf<Factory>()
        val troops = mutableListOf<Troop>()

        for (i in 0 until entityCount) {
            val entityId = input.nextInt()
            val entityType = input.next()

            val diplomacy = when (input.nextInt()) {
                1 -> Diplomacy.ALLY
                0 -> Diplomacy.NEUTRAL
                -1 -> Diplomacy.ENEMY
                else -> Diplomacy.ENEMY
            }

            val arg2 = input.nextInt()
            val arg3 = input.nextInt()
            val arg4 = input.nextInt()
            val arg5 = input.nextInt()

            when (entityType) {
                "FACTORY" -> factories.add(Factory(entityId, diplomacy, arg2, arg3))
                "TROOP" -> troops.add(Troop(entityId, diplomacy, arg4))
            }
        }

        factories.filter { it.diplomacy == Diplomacy.ALLY }?.maxBy { it.cyborgsNumber }?.let { factoryAlly ->
            factories.firstOrNull { it.diplomacy == Diplomacy.NEUTRAL }?.let { factoryEnemy ->
                println("MOVE ${factoryAlly.id} ${factoryEnemy.id} ${factoryAlly.cyborgsNumber / 2}")
            } ?: factories.filter { it.diplomacy == Diplomacy.ENEMY }.minBy { it.cyborgsNumber }?.let { factoryEnemy ->
                println("MOVE ${factoryAlly.id} ${factoryEnemy.id} ${factoryAlly.cyborgsNumber / 2}")
            } ?: println("WAIT")
        } ?: println("WAIT")

    }
}