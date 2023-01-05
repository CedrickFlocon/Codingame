package org.neige.codingame.ghostinthecell

import org.neige.codingame.util.Log
import java.util.Scanner
import kotlin.math.absoluteValue
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
                .filter { it.safeness < 0 && it.cyborgsProduction > 0 }
                .filter { factory -> factory.bombs.none { it.to == factory && it.diplomacy == Diplomacy.ALLY } }
                .sortedByDescending { it.cyborgsProduction }
                .take(Factory.BOMB_COUNT)
                .forEach { enemyFactory ->
                    enemyFactory.links
                        .filter { it.to.diplomacy == Diplomacy.ALLY }
                        .minByOrNull { it.distance }?.let {
                            actions.add(it.to.bombFactory(enemyFactory))
                        }
                }
        }

        if (allyCyborgs / (allyCyborgs + enemyCyborgs).toFloat() > 0.40 && factories.none { it.diplomacy == Diplomacy.NEUTRAL && it.cyborgsProduction > 0 && it.cyborgsNumber <= 10 }) {
            factories
                .asSequence()
                .filter { it.diplomacy == Diplomacy.ALLY }
                .filter { !it.nearBomb }
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
            .filter { it.diplomacy == Diplomacy.ALLY }
            .flatMap { it.links }
            .sortedByDescending { it.to.attractiveness * 1 / it.distance + it.to.safeness / 10 }
            .asSequence()
            .filter { it.from.cyborgsNumber > 0 && it.from.cyborgsProjection > 0 }
            .filter { it.to.diplomacy != Diplomacy.ALLY || (it.to.cyborgsProduction < 3) }
            .filter { it.to.diplomacy != Diplomacy.NEUTRAL || (it.from.cyborgsNumber > it.to.cyborgsNumber && it.to.cyborgsNumber >= it.to.cyborgsProjection) }
            .filter { it.to.diplomacy != Diplomacy.ENEMY || (it.to.cyborgsNumber < it.from.cyborgsNumber && it.to.cyborgsProjection <= it.to.cyborgsNumber) }
            .toList()
            .forEach {
                val cyborgsNumber = when {
                    it.from.nearBomb -> it.to.cyborgsNumber
                    it.to.diplomacy == Diplomacy.ALLY -> it.to.cyborgsProjection.roundToInt().absoluteValue
                    it.to.diplomacy == Diplomacy.NEUTRAL -> it.to.cyborgsNumber - it.to.cyborgsProjection.roundToInt() + 1
                    it.to.diplomacy == Diplomacy.ENEMY -> if (it.from.safeness > 0) it.from.cyborgsNumber else it.distance * it.to.cyborgsProduction + it.to.cyborgsNumber
                    else -> 0
                }
                actions.add(it.from.moveFactory(it.to, cyborgsNumber))
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
            Diplomacy.NEUTRAL -> {}
        }

        factories[entityId].newTurn(diplomacy, cyborgsNumber, cyborgsProduction, if (turnProductionStop == 0) null else turnProductionStop)
    }

    private fun createTroop(entityId: Int, diplomacy: Diplomacy, from: Factory, to: Factory, cyborgsNumber: Int, remainingDistance: Int) {
        totalCyborgs += cyborgsNumber
        when (diplomacy) {
            Diplomacy.ALLY -> allyCyborgs += cyborgsNumber
            Diplomacy.ENEMY -> enemyCyborgs += cyborgsNumber
            Diplomacy.NEUTRAL -> {}
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
