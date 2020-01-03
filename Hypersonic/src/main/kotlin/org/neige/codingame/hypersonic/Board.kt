package org.neige.codingame.hypersonic

import java.util.*
import kotlin.math.max
import kotlin.math.min
import kotlin.reflect.KClass


class Board(private val scanner: Scanner, private val width: Int, private val height: Int) {

    private val grid = Array(width) { i -> Array<Located>(height) { j -> Floor(i, j) } }
    private val players = mutableMapOf<Int, Player>()

    fun nextTurn() {
        for (y in 0 until height) {
            scanner.next().forEachIndexed { x, c ->
                when (c) {
                    '.' -> addElement(Floor(x, y))
                    'X' -> addElement(Wall(x, y))
                    '1' -> addElement(Box(x, y, ItemType.EXTRA_RANGE))
                    '2' -> addElement(Box(x, y, ItemType.EXTRA_BOMB))
                    else -> addElement(Box(x, y, ItemType.NONE))
                }
            }
        }

        val entityNumber = scanner.nextInt()
        for (i in 0 until entityNumber) {
            val entityType = scanner.nextInt()
            val owner = scanner.nextInt()
            val x = scanner.nextInt()
            val y = scanner.nextInt()
            val param1 = scanner.nextInt()
            val param2 = scanner.nextInt()

            when (entityType) {
                0 -> players[owner] = Player(owner, x, y, param1, param2)
                1 -> addElement(Bomb(owner, x, y, param1, param2))
                2 -> addElement(Item(x, y, if (param1 == 1) ItemType.EXTRA_RANGE else ItemType.EXTRA_BOMB))
            }
        }
    }

    fun getPlayer(playerId: Int): Player {
        return players[playerId]!!
    }

    fun getGridElement(located: Located): Located {
        return grid[located.x][located.y]
    }

    fun getClosestBox(located: Located): Box? {
        var box: Box? = null

        val accessiblePath = getAccessiblePath(located)

        for (x in 0 until width) {
            for (y in 0 until height) {
                val element = grid[x][y]
                if (element is Box && timerToExplode(element) == null && accessiblePath.find { it.checkNeighbour(element) } != null) {
                    if (box == null || located.distanceBetween(element) < located.distanceBetween(box)) {
                        box = element
                    }
                }
            }
        }
        return box
    }

    fun getClosestSafePlace(located: Located): Located? {
        var safePlace: Located? = null

        val accessiblePath = getAccessiblePath(located)

        for (floor in accessiblePath) {
            if (timerToExplode(floor) == null) {
                if (safePlace == null || safePlace.distanceBetween(located) > floor.distanceBetween(located)) {
                    safePlace = floor
                }
            }
        }

        return safePlace
    }

    fun timerToExplode(located: Located, fictitiousGrid: Array<Array<Located>> = grid, bombsChecked: List<Bomb> = emptyList()): Int? {
        val bombs = mutableListOf<Bomb>()
        bombs.addAll(bombsChecked)
        var minTimer: Int? = null

        if (fictitiousGrid[located.x][located.y] is Bomb) {
            minTimer = (fictitiousGrid[located.x][located.y] as Bomb).timer
        }

        for (it in located.x - 1 downTo 0) {
            val element = fictitiousGrid[it][located.y]
            if (element is Bomb && !bombs.contains(element) && element.range > located.distanceBetween(element)) {
                bombs.add(element)
                minTimer = minOf(minTimer ?: element.timer, element.timer, timerToExplode(element, fictitiousGrid, bombs) ?: element.timer)
            }
            if (element !is Floor) {
                break
            }
        }

        for (it in located.x + 1 until width) {
            val element = fictitiousGrid[it][located.y]
            if (element is Bomb && !bombs.contains(element) && element.range > located.distanceBetween(element)) {
                bombs.add(element)
                minTimer = minOf(minTimer ?: element.timer, element.timer, timerToExplode(element, fictitiousGrid, bombs) ?: element.timer)
            }
            if (element !is Floor) {
                break
            }
        }

        for (it in located.y - 1 downTo 0) {
            val element = fictitiousGrid[located.x][it]
            if (element is Bomb && !bombs.contains(element) && element.range > located.distanceBetween(element)) {
                bombs.add(element)
                minTimer = minOf(minTimer ?: element.timer, element.timer, timerToExplode(element, fictitiousGrid, bombs) ?: element.timer)
            }
            if (element !is Floor) {
                break
            }
        }

        for (it in located.y + 1 until height) {
            val element = fictitiousGrid[located.x][it]
            if (element is Bomb && !bombs.contains(element) && element.range > located.distanceBetween(element)) {
                bombs.add(element)
                minTimer = minOf(minTimer ?: element.timer, element.timer, timerToExplode(element, fictitiousGrid, bombs) ?: element.timer)
            }
            if (element !is Floor) {
                break
            }
        }

        return minTimer
    }

    fun buildPathTo(from: Located, to: Located, accessiblePathLoaded: List<Located> = emptyList(), path: Array<Located> = emptyArray()): List<Array<Located>> {
        val accessiblePath = if (accessiblePathLoaded.isEmpty()) getAccessiblePath(from) else accessiblePathLoaded
        val paths = mutableListOf<Array<Located>>()

        if (from.sameLocated(to)) {
            return listOf(path + from)
        }

        accessiblePath.filter { it.checkNeighbour(from) }.forEach { neighbour ->
            if (!path.any { it.sameLocated(neighbour) }) {
                buildPathTo(neighbour, to, accessiblePath, path + from).forEach {
                    if (it.isNotEmpty()) {
                        paths.add(it)
                    }
                }
            }
        }


        return paths
    }

    fun shortestPath(from: Located, to: Located): List<Located> {
        val validLocation = mutableListOf<List<Located>>(mutableListOf(from))

        if (from.sameLocated(to)) {
            return emptyList()
        }

        while (true) {
            val copy = validLocation.toMutableList()
            validLocation.clear()
            for (path in copy) {
                for (located in getNeighbour(path[path.size - 1]).filter { path.find { pathLocation -> pathLocation.sameLocated(it) } == null }) {
                    if (getGridElement(located) !is Box && getGridElement(located) !is Bomb && getGridElement(located) !is Wall) {
                        validLocation.add(path + located)
                        if (to.sameLocated(located)) {
                            val shortestPath = (path + located).toMutableList()
                            shortestPath.removeAt(0)
                            return shortestPath
                        }
                    }
                }
            }
        }
    }

    fun getAccessiblePath(located: Located, pathsChecked: List<Located> = emptyList()): List<Located> {
        val paths = mutableListOf<Located>()

        val element = getGridElement(located)
        if (pathsChecked.isEmpty() || element is Floor || element is Item || located is Player) {
            paths.add(element)
        } else {
            return emptyList()
        }

        val leftCoordinate = Coordinate(located.x - 1, located.y)
        if (located.x > 0 && !(paths + pathsChecked).contains(getGridElement(leftCoordinate))) {
            paths.addAll(getAccessiblePath(leftCoordinate, paths + pathsChecked))
        }

        val rightCoordinate = Coordinate(located.x + 1, located.y)
        if (located.x < width - 1 && !(paths + pathsChecked).contains(getGridElement(rightCoordinate))) {
            paths.addAll(getAccessiblePath(rightCoordinate, paths + pathsChecked))
        }

        val upCoordinate = Coordinate(located.x, located.y - 1)
        if (located.y > 0 && !(paths + pathsChecked).contains(getGridElement(upCoordinate))) {
            paths.addAll(getAccessiblePath(upCoordinate, paths + pathsChecked))
        }
        val downCoordinate = Coordinate(located.x, located.y + 1)
        if (located.y < height - 1 && !(paths + pathsChecked).contains(getGridElement(downCoordinate))) {
            paths.addAll(getAccessiblePath(downCoordinate, paths + pathsChecked))
        }

        return paths
    }

    fun destroyableBoxNumber(located: Located, bombRange: Int): Int {
        var boxNumber = 0

        for (x in located.x + 1..min(located.x + bombRange, width - 1)) {
            if (grid[x][located.y] is Box && timerToExplode(Coordinate(x, located.y)) == null) {
                boxNumber++
                break
            } else if (grid[x][located.y] !is Floor) {
                break
            }
        }

        for (x in located.x - 1 downTo max(located.x - bombRange, 0)) {
            if (grid[x][located.y] is Box && timerToExplode(Coordinate(x, located.y)) == null) {
                boxNumber++
                break
            } else if (grid[x][located.y] !is Floor) {
                break
            }
        }

        for (y in located.y + 1..min(located.y + bombRange, height - 1)) {
            if (grid[located.x][y] is Box && timerToExplode(Coordinate(located.x, y)) == null) {
                boxNumber++
                break
            } else if ((grid[located.x][y] !is Floor)) {
                break
            }
        }

        for (y in located.y - 1 downTo max(located.y - bombRange, 0)) {
            if (grid[located.x][y] is Box && timerToExplode(Coordinate(located.x, y)) == null) {
                boxNumber++
                break
            } else if (grid[located.x][y] !is Floor) {
                break
            }
        }

        return boxNumber
    }

    fun isDeadEnd(located: Located): Boolean {
        return getAccessiblePath(located).none { timerToExplode(it) == null } || getGridElement(located) is Bomb
    }

    fun isSuicide(located: Located, fictiveBomb: Bomb): Boolean {
        val old = grid[fictiveBomb.x][fictiveBomb.y]
        grid[fictiveBomb.x][fictiveBomb.y] = fictiveBomb

        val timer = getAccessiblePath(located).map { timerToExplode(it) }.distinct()

        grid[fictiveBomb.x][fictiveBomb.y] = old

        return if (timer.find { it == null } != null) {
            false
        } else {
            timer.size == 1
        }
    }

    fun <T> countElementType(vararg types: KClass<T>): Int where T : Located {
        var count = 0
        for (row in grid) {
            for (located in row) {
                if (types.contains(located::class)) {
                    count++
                }
            }
        }
        return count
    }

    fun <T> countAccessibleElementType(located: Located, vararg types: KClass<T>): Int where T : Located {
        return getAccessiblePath(located).count { types.contains(it::class) }
    }

    fun getNeighbour(located: Located): List<Located> {
        return listOf<Located>(
                Coordinate(located.x + 1, located.y),
                Coordinate(located.x - 1, located.y),
                Coordinate(located.x, located.y + 1),
                Coordinate(located.x, located.y - 1)
        ).filter { it.x >= 0 && it.y >= 0 && it.x < width && it.y < height }
    }

    private fun addElement(element: Located) {
        grid[element.x][element.y] = element
    }

}