package org.neige.codingame.ghostinthecell

data class Factory(override val id: Int, override val diplomacy: Diplomacy, val cyborgsNumber: Int, val cyborgsProduction: Int) : Entity {

}