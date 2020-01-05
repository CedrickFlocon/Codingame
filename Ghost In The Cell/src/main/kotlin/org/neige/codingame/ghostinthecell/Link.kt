package org.neige.codingame.ghostinthecell

data class Link(val from: Factory, val to: Factory, val distance: Int) {

    val attractiveness: Int

    init {
        val diplomacy = when (to.diplomacy) {
            Diplomacy.ALLY -> -10
            Diplomacy.ENEMY -> 0
            Diplomacy.NEUTRAL -> 10
        }

        attractiveness = -distance - to.cyborgsNumber + diplomacy + to.cyborgsProduction * 2
    }

}