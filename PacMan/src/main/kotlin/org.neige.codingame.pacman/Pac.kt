package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate
import java.util.*


data class Pac(
        val id: Int,
        val team: Team,
        val type: Type,
        val speedTurnsLeft: Int,
        val abilityCooldown: Int,
        override val coordinate: Coordinate,
        override var lastTurnSeen: Int
) : Element {

    var hiddenCollision = false
    val commandHistory = mutableListOf<Command>()

    var command: Command? = null
        private set

    val speed: Int
        get() = if (speedTurnsLeft > 0) 2 else 1

    companion object {
        private val TYPE_WIN = mapOf(Type.ROCK to Type.SCISSORS, Type.SCISSORS to Type.PAPER, Type.PAPER to Type.ROCK)
        private val TYPE_LOST = mapOf(Type.ROCK to Type.PAPER, Type.SCISSORS to Type.ROCK, Type.PAPER to Type.SCISSORS)

        fun readPac(input: Scanner): Pac {
            val pacId = input.nextInt()
            val team = if (input.nextInt() != 0) Team.ALLY else Team.ENEMY
            val x = input.nextInt()
            val y = input.nextInt()
            val type = when (input.next()) {
                "ROCK" -> Type.ROCK
                "SCISSORS" -> Type.SCISSORS
                "PAPER" -> Type.PAPER
                "DEAD" -> Type.DEAD
                else -> throw IllegalArgumentException()
            }
            val speedTurnsLeft = input.nextInt()
            val abilityCooldown = input.nextInt()

            return Pac(pacId, team, type, speedTurnsLeft, abilityCooldown, Coordinate(x, y), 0)
        }

    }

    fun buildCommand(board: Board) {
        if (hiddenCollision && this.abilityCooldown == 0) {
            command = switch(getVulnerability(this))
            return
        }

        val depth = when (board.height) {
            in 10..14 -> 10
            in 15..17 -> 7
            else -> throw IllegalStateException()
        } + when (board.width) {
            in 19..26 -> 10
            in 27..35 -> 7
            else -> throw IllegalStateException()
        } + when (board.alivePacs.filter { it.team == Team.ALLY }.count()) {
            1 -> 4
            2 -> 3
            3 -> 2
            4 -> 2
            5 -> 1
            else -> throw IllegalStateException()
        }

        val scoredPath = board.buildPath(coordinate, depth)
                .map { paths -> paths to pathScore(board, paths) }
                .sortedByDescending { it.second }

        val threat = (board.buildPath(coordinate, if (speed == 2) 4 else 3)
                .map {
                    it.map { board[it] }
                            .withIndex()
                            .filter { (index, element) ->
                                element is Pac && element.team == Team.ENEMY && element.type != Type.DEAD && element.lastTurnSeen < 2 && index < element.speed + speed
                            }
                }.flatten() as List<IndexedValue<Pac>>)
                .distinctBy { it.value.id }

        if (abilityCooldown == 0) {
            if (threat.none { fight(it.value) == Fight.BEATEN }) {
                command = speed()
                return
            } else if (threat.all { fight(it.value) == Fight.BEATEN }) {
                command = switch(getVulnerability(threat.first { fight(it.value) == Fight.BEATEN }.value))
                return
            }
        }

        val bestPath = scoredPath.first()
        command = move(bestPath.first, bestPath.second.toString())
    }

    fun cancelCommand() {
        command = null
    }

    private fun pathScore(board: Board, coordinates: List<Coordinate>): Double {
        val threat = if (board.neighbor(coordinates.first()).mapNotNull { board[it] }.filterIsInstance<Pac>().filter { it.lastTurnSeen == 0 }.any { fight(it) == Fight.BEATEN }) {
            -1.0
        } else {
            0.0
        }

        val lastCommand = commandHistory.firstOrNull()
        val collision = if (hiddenCollision && lastCommand is Move && lastCommand.path.first() == coordinates.first()) {
            -0.2
        } else {
            0.0
        }

        return coordinates
                .map { board[it] }
                .withIndex()
                .filter { (_, element) -> element !is Pac || element.lastTurnSeen < 2 }
                .sumByDouble { (index, element) ->
                    val distance = index + 1

                    when (element) {
                        is Pellet ->
                            if (board.pelletsPercentageLeft > 80 && element.lastTurnSeen > 20) {
                                (element.value.toDouble() / 10.0) / 10
                            } else {
                                (element.value.toDouble() / 10.0)
                            }
                        is Pac ->
                            when (element.team) {
                                Team.ALLY -> -0.3
                                Team.ENEMY ->
                                    when (fight(element)) {
                                        Fight.DRAW, Fight.BEAT ->
                                            if (element.abilityCooldown == 0) {
                                                -0.7
                                            } else if (coordinates.size < 5 && (abilityCooldown < element.abilityCooldown || fight(element) == Fight.BEAT) && coordinates.size < element.abilityCooldown) {
                                                1.0
                                            } else {
                                                -0.2
                                            }
                                        Fight.BEATEN -> -0.7

                                    }
                            }
                        else -> 0.0
                    } * coefDistance(distance)
                } + threat + collision
    }

    private fun coefDistance(distance: Int): Double {
        return 1.0 / distance
    }

    private fun move(path: List<Coordinate>, message: String? = null): Command {
        assert(this.coordinate != path.lastOrNull())
        return Move(this, path, message)
    }

    private fun switch(type: Type): Command {
        assert(this.type != type)
        assert(this.abilityCooldown == 0)
        return Switch(this, type)
    }

    private fun speed(): Command {
        assert(this.abilityCooldown == 0)
        return Speed(this)
    }

    private fun fight(enemy: Pac): Fight {
        assert(this.type != Type.DEAD)
        return when (enemy.type) {
            TYPE_WIN[type] -> Fight.BEAT
            TYPE_LOST[type] -> Fight.BEATEN
            else -> Fight.DRAW
        }
    }

    private fun getVulnerability(enemy: Pac): Type {
        assert(this.type != Type.DEAD)
        return TYPE_LOST.getValue(enemy.type)
    }

    override fun toString(): String {
        return "Pac : $id ${team.name} $coordinate ${type.name} $lastTurnSeen"
    }

    override fun equals(other: Any?): Boolean {
        return other is Pac && other.id == id && other.team == team
    }

    enum class Team {
        ALLY,
        ENEMY,
    }

    enum class Type {
        DEAD,
        ROCK,
        PAPER,
        SCISSORS,
    }

    enum class Fight {
        DRAW,
        BEAT,
        BEATEN,
    }

}
