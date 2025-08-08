package org.neige.codingame.soak

import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector
import kotlin.math.max

class Game() {
    val agents = mutableMapOf<Int, Agent>()

    val opponentAgents: Map<Int, Agent>
        get() = agents.filter { it.value.player == Player.OPPONENT }

    val myAgents: Map<Int, Agent>
        get() = agents.filter { it.value.player == Player.ME }

    private lateinit var size: Size
    private lateinit var grid: Map<Coordinate, Tile>

    fun compute(
        size: Size,
        grid: Map<Coordinate, TileType>
    ) {
        this.size = size

        val emptyPlace = grid.filter { it.value == TileType.EMPTY }

        this.grid = grid.mapValues { (coordinate, tileType) ->
            when (tileType) {
                TileType.LOW_COVER -> Tile.Cover.Low(coordinate)
                TileType.HIGH_COVER -> Tile.Cover.High(coordinate)
                TileType.EMPTY -> {
                    val damageReductionFrom = mutableMapOf<Coordinate, Float>()

                    COVER_COMPUTATION
                        .filter { grid[coordinate + it.first] != null && grid[coordinate + it.first] != TileType.EMPTY }
                        .forEach { (vector, lambda) ->
                            emptyPlace.filter { lambda(it.key, coordinate) }
                                .forEach {
                                    damageReductionFrom[it.key] = max(damageReductionFrom[it.key] ?: 0f, grid[coordinate + vector]!!.damageReduction)
                                }
                        }

                    Tile.Empty(coordinate, damageReductionFrom)
                }
            }
        }
    }

    fun play() {
        val cluster = grid.values
            .mapNotNull { tile ->
                val adjacentAgent = (ADJACENT.map { move -> agents.values.firstOrNull { it.coordinate == (tile.coordinate + move) } } +
                        agents.values.firstOrNull { it.coordinate == tile.coordinate }).filterNotNull()

                (tile to adjacentAgent).takeIf { adjacentAgent.isNotEmpty() }
            }
            .filter { it.second.none { it.player == Player.ME } }
            .maxByOrNull { it.second.size }!!

        myAgents.forEach {
            it.value.execute(Action.Move(cluster.first.coordinate), Action.Combat.Throw(cluster.first.coordinate))
        }
    }

    //Wood 2 objective
    fun bestCoverAndFire() {
        val maxDamageReduction = grid
            .mapNotNull { it.value as? Tile.Empty }
            .groupBy { tile ->
                opponentAgents
                    .mapNotNull { tile.damageReductionFrom[it.value.coordinate] }
                    .sum()
            }
            .maxBy { it.key }
            .value

        myAgents.values
            .forEach { agent ->
                val bestCover = maxDamageReduction.minBy { agent.coordinate.distanceFrom(it.coordinate) }

                val minDamageReduction = opponentAgents.values
                    .filter { it.coordinate.distanceFrom(bestCover.coordinate) < size.width / 2 }
                    .groupBy { (grid[it.coordinate] as Tile.Empty).damageReductionFrom.getOrDefault(agent.coordinate, 0f) }
                    .minBy { it.key }
                    .value
                    .first()

                agent.execute(
                    Action.Move(bestCover.coordinate),
                    Action.Combat.Shoot(minDamageReduction),
                )
            }
    }

    companion object {
        private val UP_LAMBDA: (from: Coordinate, to: Coordinate) -> Boolean = { from, to ->
            val cover = to + UP
            from.y < cover.y && from != cover + UP && from != cover + UP + LEFT && from != cover + UP + RIGHT
        }

        private val DOWN_LAMBDA: (from: Coordinate, to: Coordinate) -> Boolean = { from, to ->
            val cover = to + DOWN
            from.y > cover.y && from != cover + DOWN && from != cover + DOWN + LEFT && from != cover + DOWN + RIGHT
        }

        private val RIGHT_LAMBDA: (from: Coordinate, to: Coordinate) -> Boolean = { from, to ->
            val cover = to + RIGHT
            from.x > cover.x && from != cover + RIGHT && from != cover + RIGHT + UP && from != cover + RIGHT + DOWN
        }

        private val LEFT_LAMBDA: (from: Coordinate, to: Coordinate) -> Boolean = { from, to ->
            val cover = to + LEFT
            from.x < cover.x && from != cover + LEFT && from != cover + LEFT + UP && from != cover + LEFT + DOWN
        }

        private val UP = Vector(0, -1)
        private val DOWN = Vector(0, 1)
        private val RIGHT = Vector(1, 0)
        private val LEFT = Vector(-1, 0)

        private val ADJACENT = listOf(
            UP, UP + RIGHT, UP + LEFT,
            DOWN, DOWN + RIGHT, DOWN + LEFT,
            RIGHT,
            LEFT
        )

        private val COVER_COMPUTATION = listOf(
            UP to UP_LAMBDA,
            DOWN to DOWN_LAMBDA,
            LEFT to LEFT_LAMBDA,
            RIGHT to RIGHT_LAMBDA
        )
    }
}

enum class TileType(val damageReduction: Float) {
    EMPTY(Float.NaN),
    LOW_COVER(0.5f),
    HIGH_COVER(0.75f);

    companion object {
        fun fromInt(int: Int): TileType {
            return when (int) {
                0 -> EMPTY
                1 -> LOW_COVER
                2 -> HIGH_COVER
                else -> throw IllegalArgumentException("invalid input $int")
            }
        }
    }
}

data class Size(
    val width: Int, val height: Int
)

sealed interface Tile {
    val coordinate: Coordinate

    sealed interface Cover : Tile {
        data class Low(override val coordinate: Coordinate) : Cover
        data class High(override val coordinate: Coordinate) : Cover
    }

    data class Empty(
        override val coordinate: Coordinate,
        val damageReductionFrom: Map<Coordinate, Float>
    ) : Tile {
        override fun toString(): String {
            return "$coordinate"
        }
    }
}
