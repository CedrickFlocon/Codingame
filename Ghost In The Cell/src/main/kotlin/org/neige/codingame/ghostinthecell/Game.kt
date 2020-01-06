package org.neige.codingame.ghostinthecell

import java.util.*
import kotlin.math.min
import kotlin.math.roundToInt

/**
 *
 *  One game turn is computed as follows:
 *
 * Move existing troops and bombs
 * Execute user orders
 * Produce new cyborgs in all factories
 * Solve battles
 * Make the bombs explode
 * Check end conditions
 *
 */
class Game(private val input: Scanner, private val factories: Array<Factory>) {

    private var turnCount = -1

    private var allyProduction = 0
    private var enemyProduction = 0
    private var totalProduction: Int = 0

    private var allyCyborgs = 0
    private var enemyCyborgs = 0
    private var totalCyborgs = 0

    private val bombs = mutableSetOf<Bomb>()

    fun nextTurn() {
        turnCount++

        totalProduction = 0
        allyProduction = 0
        enemyProduction = 0

        allyCyborgs = 0
        enemyCyborgs = 0
        totalCyborgs = 0

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
                "FACTORY" -> createFactory(entityId, diplomacy, arg2, arg3, arg4)
                "TROOP" -> createTroop(entityId, diplomacy, factories[arg2], factories[arg3], arg4, arg5)
                "BOMB" -> createBomb(entityId, diplomacy, factories[arg2], if (arg3 == -1) null else factories[arg3], if (arg4 == -1) null else arg4)
            }
        }

        Log.debug(this.toString())

        val warningFactories = factories
                .filter { it.diplomacy == Diplomacy.ENEMY }
                .flatMap { factory -> factory.bombs.filter { it.diplomacy == Diplomacy.ENEMY } }
                .flatMap { bomb -> bomb.from.links.filter { it.distance == bomb.distanceTraveled && it.to.diplomacy == Diplomacy.ALLY } }
                .map { it.to }

        factories.forEach {
            it.buildCoefficient(warningFactories.contains(it))
            Log.debug(it.toString())
        }
    }

    fun play(): List<Action> {
        val actions = mutableListOf<Action>()

        if ((turnCount == 0 || turnCount >= 5) && Factory.canLaunchBomb()) {
            factories
                    .filter { it.diplomacy == Diplomacy.ENEMY }
                    .filter { it.cyborgsProjection < 0 }
                    .filter { factory -> factory.bombs.none { it.to == factory && it.diplomacy == Diplomacy.ALLY } }
                    .sortedByDescending { it.cyborgsProduction }
                    .take(Factory.BOMB_COUNT)
                    .forEach { enemyFactory ->
                        enemyFactory.links
                                .filter { it.to.diplomacy == Diplomacy.ALLY }
                                .minBy { it.distance }?.let {
                                    actions.add(it.to.bombFactory(enemyFactory))
                                }
                    }
        }

        if (allyCyborgs / (allyCyborgs + enemyCyborgs).toFloat() > 0.45) {
            factories
                    .filter { it.diplomacy == Diplomacy.ALLY }
                    .filter { it.cyborgsNumber >= 10 }
                    .filter { it.safeness > 0 }
                    .filter { it.cyborgsProjection > 10 }
                    .filter { it.cyborgsProduction < 3 }
                    .sortedByDescending { it.safeness }
                    .take(1)
                    .forEach {
                        actions.add(it.increase())
                    }
        }

        factories
                .flatMap { it.links }
                .sortedByDescending { it.to.attractiveness * 1 / it.distance }
                .asSequence()
                .filter { it.to.diplomacy == Diplomacy.ALLY }
                .filter { it.to.cyborgsProjection > 0 }
                .filter { it.from.safeness < it.to.safeness }
                .filter { it.from.diplomacy != Diplomacy.NEUTRAL || it.from.cyborgsNumber < it.to.cyborgsProjection }
                .forEach {
                    when {
                        it.to.nearBomb -> actions.add(it.to.moveFactory(it.from, it.to.cyborgsNumber))
                        else -> actions.add(it.to.moveFactory(it.from, min(it.to.cyborgsProjection.roundToInt(), it.from.cyborgsNumber + 1)))
                    }
                }

        return actions
    }

    override fun toString(): String {
        return """
            | Game
            | => P ${allyProduction - enemyProduction} C ${allyCyborgs - enemyCyborgs}
            | => TotalProduction $totalProduction | TotalCyborgs $totalCyborgs
            """.trimMargin()
    }

    private fun createFactory(entityId: Int, diplomacy: Diplomacy, cyborgsNumber: Int, cyborgsProduction: Int, turnProductionStop: Int) {
        totalCyborgs += cyborgsNumber
        totalProduction += cyborgsProduction

        when (diplomacy) {
            Diplomacy.ALLY -> {
                allyCyborgs += cyborgsNumber
                allyProduction += if (turnProductionStop == 0) cyborgsProduction else 0
            }
            Diplomacy.ENEMY -> {
                enemyCyborgs += cyborgsNumber
                enemyProduction += if (turnProductionStop == 0) cyborgsProduction else 0
            }
        }

        factories[entityId].newTurn(diplomacy, cyborgsNumber, cyborgsProduction, if (turnProductionStop == 0) null else turnProductionStop)
    }

    private fun createTroop(entityId: Int, diplomacy: Diplomacy, from: Factory, to: Factory, cyborgsNumber: Int, remainingDistance: Int) {
        totalCyborgs += cyborgsNumber
        when (diplomacy) {
            Diplomacy.ALLY -> allyCyborgs += cyborgsNumber
            Diplomacy.ENEMY -> enemyCyborgs += cyborgsNumber
        }

        val troop = Troop(entityId, diplomacy, from, to, cyborgsNumber, remainingDistance)
        from.troops.add(troop)
        to.troops.add(troop)
    }

    private fun createBomb(entityId: Int, diplomacy: Diplomacy, from: Factory, to: Factory?, distance: Int?) {
        (bombs.find { it.id == entityId }?.also {
            it.move()
        } ?: with(Bomb(entityId, diplomacy, from, to, distance)) {
            bombs.add(this)
            this.move()
            this
        }).let {
            from.bombs.add(it)
            to?.bombs?.add(it)
        }
    }

}
