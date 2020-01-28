package org.neige.codingame.ghostinthecell

import kotlin.math.max

data class Factory(override val id: Int) : Entity {

    companion object {

        var BOMB_COUNT = 2

        fun canLaunchBomb(): Boolean {
            return BOMB_COUNT > 0
        }
    }

    val links = mutableListOf<Link>()
    var distanceToCenter = 0

    override var diplomacy: Diplomacy = Diplomacy.NEUTRAL
        private set
    var cyborgsNumber: Int = 0
        private set
    var cyborgsProduction: Int = 0
        private set
    var turnProductionStop: Int? = null

    val troops = mutableListOf<Troop>()
    val bombs = mutableListOf<Bomb>()

    var attractiveness: Double = 0.0
        private set
    var safeness: Double = 0.0
        private set
    var cyborgsProjection = 0.0
        private set
    var nearBomb = false
        private set

    fun newTurn(diplomacy: Diplomacy, cyborgsNumber: Int, cyborgsProduction: Int, turnProductionStop: Int?) {
        troops.clear()
        bombs.clear()

        this.diplomacy = diplomacy
        this.cyborgsNumber = cyborgsNumber
        this.cyborgsProduction = cyborgsProduction
        this.turnProductionStop = turnProductionStop
    }

    fun buildCoefficient(nearBomb: Boolean) {
        this.nearBomb = nearBomb
        this.safeness = buildSafeness()
        this.cyborgsProjection = buildProjection()
        this.attractiveness = buildAttractiveness()
    }

    fun moveFactory(factory: Factory, cyborgsNumber: Int): Action {
        assertAlly()

        val troop = Troop(-1, Diplomacy.ALLY, this, factory, cyborgsNumber, links.first { it.to == factory }.distance)
        this.troops.add(troop)
        factory.troops.add(troop)

        this.cyborgsNumber = max(this.cyborgsNumber - cyborgsNumber, 0)
        return Moving(troop)
    }

    fun bombFactory(factory: Factory): Action {
        assertAlly()

        val bomb = Bomb(-1, Diplomacy.ALLY, this, factory, links.first { it.to == factory }.distance)
        bombs.add(bomb)
        factory.bombs.add(bomb)

        return Bombing(this, factory)
    }

    fun increase(): Action {
        assertAlly()

        if (cyborgsNumber < 10) {
            throw UnsupportedOperationException("Not enough cyborgs")
        }

        this.cyborgsNumber -= 10
        return Increasing(this)
    }

    override fun toString(): String {
        return """
            | Factory $id $diplomacy distanceToCenter:$distanceToCenter
            | => cyborgsNumber:$cyborgsNumber cyborgsProduction:$cyborgsProduction
            | => nearBomb:$nearBomb attractiveness:${attractiveness.toInt()} safeness:${safeness.toInt()} cyborgsProjection:$cyborgsProjection
        """.trimMargin()
    }

    private fun assertAlly() {
        if (diplomacy != Diplomacy.ALLY) {
            throw UnsupportedOperationException("Unable to do action with a non ally factory")
        }
    }

    private fun buildSafeness(): Double {
        val cyborgsAlly = links
                .filter { it.to.diplomacy == Diplomacy.ALLY }
                .sumByDouble { it.to.cyborgsNumber * 1.0 / it.distance }
                .plus(if (diplomacy == Diplomacy.ALLY) cyborgsNumber else 0)

        val troopsAlly = troops.filter { it.diplomacy == Diplomacy.ALLY }
                .sumByDouble { troop -> troop.cyborgsNumber * 1.0 / troop.remainingDistance.plus(if (troop.to != this) troop.to.links.find { it.to == this }!!.distance else 0) }

        val cyborgsEnemy = links
                .filter { it.to.diplomacy == Diplomacy.ENEMY }
                .sumByDouble { it.to.cyborgsNumber * 1.0 / it.distance }
                .plus(if (diplomacy == Diplomacy.ENEMY) cyborgsNumber else 0)

        val troopsEnemy = troops.filter { it.diplomacy == Diplomacy.ENEMY }
                .sumByDouble { troop -> troop.cyborgsNumber * 1.0 / troop.remainingDistance.plus(if (troop.to != this) troop.to.links.find { it.to == this }!!.distance else 0) }

        return cyborgsAlly.plus(troopsAlly)
                .minus(cyborgsEnemy.plus(troopsEnemy))
    }

    private fun buildAttractiveness(): Double {
        return cyborgsProduction.toDouble() - cyborgsNumber.toDouble() / 10
    }

    private fun buildProjection(): Double {
        return when (diplomacy) {
            Diplomacy.ALLY -> +cyborgsNumber + if (turnProductionStop == null) cyborgsProduction else 0
            Diplomacy.ENEMY -> -cyborgsNumber - if (turnProductionStop == null) cyborgsProduction else 0
            else -> 0
        }.plus(
                troops
                        .filter { it.diplomacy == Diplomacy.ALLY }
                        .filter { it.to == this }
                        .sumByDouble { it.cyborgsNumber.toDouble() }
        ).minus(
                troops
                        .filter { it.diplomacy == Diplomacy.ENEMY }
                        .filter { it.to == this }
                        .sumByDouble { it.cyborgsNumber.toDouble() }
        )
    }

    private class Moving(private val troop: Troop) : Action() {

        override fun play(): String {
            return "MOVE ${troop.from.id} ${troop.to.id} ${troop.cyborgsNumber}"
        }

    }

    private class Bombing(private val from: Factory, private val to: Factory) : Action() {

        init {
            if (BOMB_COUNT > 0) {
                BOMB_COUNT--
            } else {
                throw UnsupportedOperationException("Unable to launch bomb")
            }
        }

        override fun play(): String {
            return "BOMB ${from.id} ${to.id}"
        }

    }

    private class Increasing(private val factory: Factory) : Action() {

        override fun play(): String {
            return "INC ${factory.id}"
        }

    }

}
