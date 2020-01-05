package org.neige.codingame.ghostinthecell

import java.util.*

class Game(private val input: Scanner, private val factories: Array<Factory>) {

    private var turnCount = 0

    fun nextTurn() {
        turnCount++

        for (i in 0 until input.nextInt()) {
            val entityId = input.nextInt()
            val entityType = input.next()

            val arg1 = input.nextInt()
            val arg2 = input.nextInt()
            val arg3 = input.nextInt()
            val arg4 = input.nextInt()
            val arg5 = input.nextInt()

            val diplomacy = when (arg1) {
                1 -> Diplomacy.ALLY
                0 -> Diplomacy.NEUTRAL
                -1 -> Diplomacy.ENEMY
                else -> Diplomacy.ENEMY
            }

            when (entityType) {
                "FACTORY" -> factories[entityId].newTurn(diplomacy, arg2, arg3, if (arg4 == 0) null else arg4)
                "TROOP" -> {
                    factories[arg2].troops.add(Troop(entityId, diplomacy, factories[arg2], factories[arg3], arg4, arg5))
                    factories[arg3].troops.add(Troop(entityId, diplomacy, factories[arg2], factories[arg3], arg4, arg5))
                }
                "BOMB" -> {
                    val factoryFrom = factories[arg2]
                    val factoryTo = if (arg3 == -1) null else factories[arg3]
                    val distance = if (arg4 == -1) null else arg4
                    factoryFrom.bombs.add(Bomb(entityId, diplomacy, factoryFrom, factoryTo, distance))
                    factoryTo?.bombs?.add(Bomb(entityId, diplomacy, factoryFrom, factoryTo, distance))
                }
            }
        }
    }

    fun play() {
        val actions = mutableListOf<Action>(Waiting)

        if (turnCount == 10 && Factory.canLaunchBomb()) {
            factories
                    .filter { it.diplomacy == Diplomacy.ALLY }
                    .flatMap { it.links }
                    .filter { it.to.diplomacy == Diplomacy.ENEMY }
                    .sortedByDescending { it.to.cyborgsProduction }
                    .distinctBy { it.to }
                    .take(2)
                    .forEach {
                        actions.add(it.from.bombFactory(it.to))
                    }
        }

        factories.filter { it.diplomacy == Diplomacy.ALLY }
                .flatMap { it.links }
                .filter { it.attractiveness > 0 }
                .sortedByDescending { it.attractiveness }
                .filter { it.from.cyborgsNumber > it.to.cyborgsNumber }
                .filter { link ->
                    link.to.troops
                            .filter { it.diplomacy == Diplomacy.ALLY }
                            .filter { it.to == link.to }
                            .sumBy { it.cyborgsNumber } < link.to.cyborgsNumber + 1
                }.forEach attack@{
                    actions.add(it.from.attackFactory(it.to, it.to.cyborgsNumber + 1))
                }

        println(actions.joinToString(";") { it.play() })
    }

}