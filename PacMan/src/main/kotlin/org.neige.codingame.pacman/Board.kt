package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector
import org.neige.codingame.geometry.get
import org.neige.codingame.geometry.set
import org.neige.codingame.util.Log


class Board(private val grid: Array<Array<Cell>>) {

    private val pacs = mutableListOf<Pac>()
    private val pacsAlly get() = pacs.filter { it.team == Pac.Team.ALLY }

    private val pellets = Array(grid.size) { x ->
        Array(grid[0].size) { y ->
            if (grid[x][y] is Floor) {
                Pellet(1, Coordinate(x, y))
            } else {
                null
            }
        }
    }

    fun paths(): List<Coordinate> {
        return emptyList()
    }

    fun updateInfo(pacs: Array<Pac>, pellets: Array<Pellet>) {
        this.pacs.clear()
        this.pacs.addAll(pacs)

        pellets.forEach {
            this.pellets[it.coordinate] = it
        }

        this.pellets
                .flatten()
                .filterNotNull()
                .filter { it.value == Pellet.SUPER_PELLETS }
                .filter { pellet -> pellets.none { it == pellet } }
                .forEach {
                    this.pellets[it.coordinate] = null
                }

        pacsAlly.forEach { pac ->
            this.pellets[pac.coordinate] = null

            (straightLine(pac.coordinate, Direction.UP) + straightLine(pac.coordinate, Direction.DOWN) +
                    straightLine(pac.coordinate, Direction.LEFT) + straightLine(pac.coordinate, Direction.RIGHT))
                    .forEach { coordinate ->
                        if (pellets.none { pellet -> pellet.coordinate == coordinate }) {
                            this.pellets[coordinate] = null
                        }
                    }
        }
    }

    fun neighbor(coordinate: Coordinate): List<Coordinate> {
        return listOf(
                grid[move(coordinate, Direction.LEFT)],
                grid[move(coordinate, Direction.UP)],
                grid[move(coordinate, Direction.RIGHT)],
                grid[move(coordinate, Direction.DOWN)]
        ).filterIsInstance<Floor>()
                .map { it.coordinate }
    }

    fun closestPellet(coordinate: Coordinate): Pellet {
        return pellets
                .flatten()
                .filterNotNull()
                .minBy { it.coordinate.distanceFrom(coordinate) }!!
    }

    fun straightLinePellet(coordinate: Coordinate, direction: Direction): List<Pellet> {
        return straightLine(coordinate, direction)
                .mapNotNull { pellets[it] }
    }

    private fun straightLine(coordinate: Coordinate, direction: Direction): List<Coordinate> {
        val straightLineCoordinate = mutableListOf<Coordinate>()

        var newCoordinate = move(coordinate, direction)

        while (grid[newCoordinate] is Floor) {
            straightLineCoordinate.add(newCoordinate)

            newCoordinate = move(newCoordinate, direction)
        }
        return straightLineCoordinate
    }

    private fun move(newCoordinate: Coordinate, direction: Direction): Coordinate {
        var newCoordinate1 = newCoordinate
        newCoordinate1 =
                if (newCoordinate1.x == 0 && direction == Direction.LEFT) {
                    Coordinate(grid.size - 1, newCoordinate1.y)
                } else if (newCoordinate1.x == grid.size - 1 && direction == Direction.RIGHT) {
                    Coordinate(0, newCoordinate1.y)
                } else {
                    newCoordinate1 + direction.vector
                }
        return newCoordinate1
    }

    fun debug() {
        Log.debug(pellets) { pellet -> if (pellet == null) " " else if (pellet.value == Pellet.SUPER_PELLETS) "@" else "*" }
    }

    enum class Direction(val vector: Vector) {
        UP(Vector(0, -1)),
        LEFT(Vector(-1, 0)),
        DOWN(Vector(0, +1)),
        RIGHT(Vector(+1, 0)),
    }

}
