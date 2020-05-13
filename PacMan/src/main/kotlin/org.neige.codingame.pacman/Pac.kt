package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate


class Pac(
        val id: Int,
        val team: Team,
        val type: Type,
        val speedTurnsLeft: Int,
        val abilityCooldown: Int,
        override val coordinate: Coordinate
) : Element {

    companion object {
        private val TYPE_WIN = mapOf(Type.ROCK to Type.SCISSORS, Type.SCISSORS to Type.PAPER, Type.PAPER to Type.ROCK)
        private val TYPE_LOST = mapOf(Type.ROCK to Type.PAPER, Type.SCISSORS to Type.ROCK, Type.PAPER to Type.SCISSORS)
    }

    fun action(board: Board): Command? {
        val straightLineElement = listOf(board.straightLineElement(this.coordinate, Board.Direction.LEFT),
                board.straightLineElement(this.coordinate, Board.Direction.UP),
                board.straightLineElement(this.coordinate, Board.Direction.RIGHT),
                board.straightLineElement(this.coordinate, Board.Direction.DOWN))

        val pacs = straightLineElement.flatten().filterIsInstance<Pac>()

        val straightLineEnemy = pacs.filter { it.team == Team.ENEMY }
        val straightLineAlly = pacs.filter { it.team == Team.ALLY }
        val straightLinePellet = straightLineElement.map { it.filterIsInstance<Pellet>() }

        val closestEnemy = straightLineEnemy
                .sortedBy { coordinate.distanceFrom(it.coordinate) }
                .firstOrNull { it.coordinate.distanceFrom(this.coordinate) < 3 }

        if (closestEnemy != null) {
            if (abilityCooldown == 0 && fight(closestEnemy) == Fight.BEATEN) {
                return switch(getVulnerability(closestEnemy))
            }
        }

        val maxPellet = straightLineElement.maxBy {
            it.sumBy { element ->
                ((when (element) {
                    is Pellet -> element.value
                    is Pac -> {
                        when (element.team) {
                            Team.ALLY -> -1
                            Team.ENEMY -> {
                                if (element.abilityCooldown == 0 && abilityCooldown != 0) {
                                    -2
                                } else {
                                    when (fight(element)) {
                                        Fight.DRAW -> {
                                            if (element.abilityCooldown > abilityCooldown) {
                                                0
                                            } else {
                                                -1
                                            }
                                        }
                                        Fight.BEAT -> 2
                                        Fight.BEATEN -> -2
                                    }
                                }
                            }
                        }
                    }
                    else -> 0
                }).toDouble() * 1.0 / element.coordinate.distanceFrom(this.coordinate) * 100).toInt()
            }
        }!!

        return if (maxPellet.size > 4 && abilityCooldown == 0 && straightLineEnemy.isEmpty()) {
            speed()
        } else if (maxPellet.isNotEmpty()) {
            move((if (maxPellet.size > 2) maxPellet[1] else maxPellet.first()).coordinate)
        } else {
            return move(board.closestPellet(coordinate).coordinate)
        }

    }

    private fun move(coordinate: Coordinate, message: String = coordinate.toString()): Command {
        assert(this.coordinate != coordinate)
        return Move(id, coordinate, message)
    }

    private fun switch(type: Type): Command {
        assert(this.type != type)
        assert(this.abilityCooldown == 0)
        return Switch(id, type)
    }

    private fun speed(): Command {
        assert(this.abilityCooldown == 0)
        return Speed(id)
    }

    private fun fight(enemy: Pac): Fight {
        return when (enemy.type) {
            TYPE_WIN[type] -> Fight.BEAT
            TYPE_LOST[type] -> Fight.BEATEN
            else -> Fight.DRAW
        }
    }

    private fun getVulnerability(enemy: Pac): Type {
        return TYPE_LOST.getValue(enemy.type)
    }

    override fun toString(): String {
        return "Pac : $id $coordinate"
    }

    enum class Team {
        ALLY,
        ENEMY
    }

    enum class Type {
        ROCK,
        PAPER,
        SCISSORS
    }

    enum class Fight {
        DRAW,
        BEAT,
        BEATEN,
    }

}
