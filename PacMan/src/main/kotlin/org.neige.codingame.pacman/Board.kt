package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector
import org.neige.codingame.geometry.get
import org.neige.codingame.geometry.set
import org.neige.codingame.util.Log

class Board(private val grid: Array<Array<Cell>>) {

    private val pacs = mutableListOf<Pac>()

    private val pellets = Array(grid.size) { x ->
        Array(grid[0].size) { y ->
            if (grid[x][y] is Floor) {
                Pellet(1, Coordinate(x, y), 0)
            } else {
                null
            }
        }
    }

    fun updateInfo(knowPacs: Array<Pac>, knowPellets: Array<Pellet>) {
        //Refresh know value
        this.pacs.clear()
        this.pacs.addAll(knowPacs)

        this.pellets.flatten().forEach { it?.let { it.lastTurnSeen++ } }
        knowPellets.forEach { this.pellets[it.coordinate] = it }

        //clear super pellets
        this.pellets
                .flatten()
                .filterNotNull()
                .filter { it.value == Pellet.SUPER_PELLETS }
                .filter { pellet -> knowPellets.none { it.coordinate == pellet.coordinate } }
                .forEach {
                    this.pellets[it.coordinate] = null
                }

        //clear pellets
        this.pacs.filter { it.team == Pac.Team.ALLY }.forEach { pac ->
            this.pellets[pac.coordinate] = null
            (straightLine(pac.coordinate, Direction.UP) + straightLine(pac.coordinate, Direction.DOWN) +
                    straightLine(pac.coordinate, Direction.LEFT) + straightLine(pac.coordinate, Direction.RIGHT))
                    .forEach { coordinate ->
                        if (knowPellets.none { pellet -> pellet.coordinate == coordinate }) {
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

    fun straightLineElement(coordinate: Coordinate, direction: Direction): List<Element> {
        return straightLine(coordinate, direction)
                .flatMap { straightLineCoordinate ->
                    listOfNotNull(pacs.firstOrNull { it.coordinate == straightLineCoordinate }, pellets[straightLineCoordinate])
                }
    }

    private fun straightLine(coordinate: Coordinate, direction: Direction): List<Coordinate> {
        val straightLineCoordinate = mutableListOf<Coordinate>()

        var newCoordinate = move(coordinate, direction)

        while (grid[newCoordinate] is Floor) {
            if (newCoordinate == coordinate) {
                break
            }
            straightLineCoordinate.add(newCoordinate)

            newCoordinate = move(newCoordinate, direction)
        }
        return straightLineCoordinate
    }

    private fun move(coordinate: Coordinate, direction: Direction): Coordinate {
        return if (coordinate.x == 0 && direction == Direction.LEFT) {
            Coordinate(grid.size - 1, coordinate.y)
        } else if (coordinate.x == grid.size - 1 && direction == Direction.RIGHT) {
            Coordinate(0, coordinate.y)
        } else {
            coordinate + direction.vector
        }
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
