package org.neige.codingame.soak

data class Agent(
    val id: Int,
    val player: Player,
    var coordinate: Coordinate = Coordinate(0, 0),
    val optimalRange: Int,
    val soakingPower: Int,
    var wetness: Int = 0,
    var splashBombs: Int = 0,
    var cooldown: Int = 0,
    var actions: List<Action> = emptyList()
) {

    private val halfSoakingPower = soakingPower / 2

    fun update(
        coordinate: Coordinate,
        wetness: Int,
        splashBombs: Int,
        cooldown: Int,
    ) {
        this.coordinate = coordinate
        this.wetness = wetness
        this.splashBombs = splashBombs
        this.cooldown = cooldown
        this.actions = emptyList()
    }

    fun shotPower(coordinate: Coordinate): Int {
        return (this.coordinate.distanceFrom(coordinate) / optimalRange).let {
            if (it <= 1) {
                soakingPower
            } else if (it <= 2) {
                halfSoakingPower
            } else {
                0
            }
        }
    }

    fun execute(move: Action.Move? = null, combat: Action.Combat? = null, message: Action.Message? = null) {
        this.actions = listOfNotNull(move, combat, message)
        println("$id; ${actions.joinToString(";")}")
    }
}

enum class Player {
    ME,
    OPPONENT;
}

sealed interface Action {
    data class Move(private val coordinate: Coordinate) : Action {
        override fun toString(): String {
            return "MOVE ${coordinate.x} ${coordinate.y}"
        }
    }

    sealed interface Combat : Action {
        data class Shoot(private val agent: Agent) : Combat {
            override fun toString(): String {
                return "SHOOT ${agent.id}"
            }
        }

        data class Throw(private val coordinate: Coordinate) : Combat {
            override fun toString(): String {
                return "THROW ${coordinate.x} ${coordinate.y}"
            }
        }

        object HunkerDown : Combat {
            override fun toString(): String {
                return "HUNKER_DOWN"
            }
        }
    }

    data class Message(val text: String) : Action {
        override fun toString(): String {
            return text
        }
    }
}
