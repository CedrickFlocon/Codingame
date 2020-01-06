package org.neige.codingame.ghostinthecell

data class Factory(override val id: Int) : Entity {

    companion object {

        private var BOMB_COUNT = 2

        fun canLaunchBomb(): Boolean {
            return BOMB_COUNT > 0
        }
    }

    val links = mutableListOf<Link>()

    override var diplomacy: Diplomacy = Diplomacy.NEUTRAL
        private set
    var cyborgsNumber: Int = 0
        private set
    var cyborgsProduction: Int = 0
        private set
    var turnStopProduction: Int? = null

    val troops = mutableListOf<Troop>()
    val bombs = mutableListOf<Bomb>()

    fun newTurn(diplomacy: Diplomacy, cyborgsNumber: Int, cyborgsProduction: Int, turnStopProduction: Int?) {
        troops.clear()
        bombs.clear()

        this.diplomacy = diplomacy
        this.cyborgsNumber = cyborgsNumber
        this.cyborgsProduction = cyborgsProduction
        this.turnStopProduction = turnStopProduction
    }

    fun attackFactory(factory: Factory, cyborgsNumber: Int): Action {
        assertAlly()

        val troop = Troop(-1, Diplomacy.ALLY, this, factory, cyborgsNumber, links.first { it.to == factory }.distance)
        this.troops.add(troop)
        factory.troops.add(troop)

        this.cyborgsNumber - cyborgsNumber
        return Attacking(troop)
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

        return Increasing(this)
    }

    override fun toString(): String {
        return "Factory id:$id diplomacy:$diplomacy cyborgsNumber:$cyborgsNumber cyborgsProduction:$cyborgsProduction"
    }

    private fun assertAlly() {
        if (diplomacy != Diplomacy.ALLY) {
            throw UnsupportedOperationException("Unable to do action with a non ally factory")
        }
    }

    private class Attacking(private val troop: Troop) : Action() {

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
