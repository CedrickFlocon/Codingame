package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector
import org.neige.codingame.geometry.get
import org.neige.codingame.geometry.set
import org.neige.codingame.util.Log
import java.util.*

class Board(private val grid: Array<Array<Cell>>) {

    companion object {

        fun readGrid(scanner: Scanner): Array<Array<Cell>> {
            val width = scanner.nextInt()
            val height = scanner.nextInt()
            if (scanner.hasNextLine()) {
                scanner.nextLine()
            }
            val rows = mutableListOf<String>()
            for (i in 0 until height) {
                rows.add(scanner.nextLine())
            }

            return Array(width) { x ->
                Array(height) { y ->
                    val coordinate = Coordinate(x, y)
                    when (rows[y][x]) {
                        '#' -> Wall(coordinate)
                        ' ' -> Floor(coordinate)
                        else -> throw IllegalArgumentException()
                    }
                }
            }
        }

    }

    private val pacs = mutableListOf<Pac>()
    val alivePacs
        get() = pacs.filter { it.type != Pac.Type.DEAD }

    private val pellets = Array(grid.size) { x ->
        Array(grid[0].size) { y ->
            if (grid[x][y] is Floor) {
                Pellet(1, Coordinate(x, y), 0)
            } else {
                null
            }
        }
    }

    private val score = mutableMapOf<Pac.Team, Int>()
    private var totalPoint = 0

    val width: Int
        get() = grid.size

    val height: Int
        get() = grid[0].size

    val pelletsPercentageLeft
        get() = score.values.sum() * 100 / totalPoint

    fun updateInfo(knowPacs: List<Pac>, knowPellets: List<Pellet>, score: Map<Pac.Team, Int>) {
        score.forEach {
            this.score[it.key] = it.value
        }

        //Refresh know value
        if (pacs.isEmpty()) {
            knowPacs
                    .filter { it.team == Pac.Team.ALLY }
                    .forEach {
                        this.pacs.add(it)
                        this.pacs.add(it.copy(team = Pac.Team.ENEMY, coordinate = (it.coordinate + Vector((grid.size / 2 - it.coordinate.x) * 2, 0))))
                    }
        } else {
            this.pacs.forEach { it.lastTurnSeen++ }
            knowPacs.forEach { knowPac ->
                this.pacs.withIndex().find { it.value == knowPac }?.let {
                    if (it.value.command is Move && knowPac.coordinate == it.value.coordinate) {
                        knowPac.hiddenCollision = true
                        knowPac.commandHistory.add(it.value.command!!)
                    }
                    this.pacs.removeAt(it.index)
                }
                this.pacs.add(knowPac)
            }
        }

        //clear pellets and enemy pacs
        knowPacs.filter { it.team == Pac.Team.ALLY && it.type != Pac.Type.DEAD }
                .forEach { pac ->
                    (listOf(pac.coordinate) +
                            straightLine(pac.coordinate, Direction.UP) +
                            straightLine(pac.coordinate, Direction.DOWN) +
                            straightLine(pac.coordinate, Direction.LEFT) +
                            straightLine(pac.coordinate, Direction.RIGHT))
                            .forEach { coordinate ->
                                this.pellets[coordinate] = null
                            }
                }


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

        if (totalPoint == 0) {
            totalPoint = pellets.flatten().filterNotNull().sumBy { it.value }
        }
    }

    fun buildPath(coordinate: Coordinate, depth: Int = 10): List<List<Coordinate>> {
        var paths = mutableListOf(listOf(coordinate))
        val finalPath = mutableListOf<List<Coordinate>>()

        for (x in 0 until depth) {
            val forkedPath = mutableListOf<List<Coordinate>>()

            for (path in paths) {
                val neighborFloor = neighbor(path.last())
                        .map { grid[it] }
                        .filterIsInstance<Floor>()
                        .filter { floor -> path.none { it == floor.coordinate } }
                        .onEach { forkedPath.add(path + it.coordinate) }

                if (neighborFloor.isEmpty()) {
                    finalPath.add(path)
                }
            }

            paths = forkedPath
        }

        return (paths + finalPath).map { it.drop(1) }
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

    fun neighbor(coordinate: Coordinate): List<Coordinate> {
        return listOf(
                grid[move(coordinate, Direction.LEFT)],
                grid[move(coordinate, Direction.UP)],
                grid[move(coordinate, Direction.RIGHT)],
                grid[move(coordinate, Direction.DOWN)]
        ).filterIsInstance<Floor>()
                .map { it.coordinate }
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

    operator fun get(coordinate: Coordinate): Element? {
        return alivePacs.find { it.coordinate == coordinate } ?: pellets[coordinate]
    }

    fun debug() {
        Log.debug(grid) { cell ->
            when (val element = pacs.find { it.coordinate == cell.coordinate } ?: pellets[cell.coordinate]) {
                is Pellet -> if (element.value == Pellet.SUPER_PELLETS) "@" else "*"
                is Pac -> if (element.type != Pac.Type.DEAD) if (element.team == Pac.Team.ALLY) "A" else "E" else "D"
                else -> " "
            }
        }

        Log.debug("$pelletsPercentageLeft%")
    }

    enum class Direction(val vector: Vector) {
        UP(Vector(0, -1)),
        LEFT(Vector(-1, 0)),
        DOWN(Vector(0, +1)),
        RIGHT(Vector(+1, 0)),
    }

}
