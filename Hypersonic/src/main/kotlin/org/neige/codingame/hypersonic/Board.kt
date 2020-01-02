package org.neige.codingame.hypersonic

import java.util.*


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
                0 -> players.put(owner, Player(owner, x, y, param1, param2))
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

    fun timerToExplode(located: Located, bombsChecked: List<Bomb> = emptyList()): Int? {
        val bombs = mutableListOf<Bomb>()
        bombs.addAll(bombsChecked)
        var minTimer: Int? = null

        if (grid[located.x][located.y] is Bomb) {
            minTimer = (grid[located.x][located.y] as Bomb).timer
        }

        for (it in located.x - 1 downTo 0) {
            val element = grid[it][located.y]
            if (element is Bomb && !bombs.contains(element) && element.range > located.distanceBetween(element)) {
                bombs.add(element)
                minTimer = minOf(minTimer ?: element.timer, element.timer, timerToExplode(element, bombs) ?: element.timer)
            }
            if (element !is Floor) {
                break
            }
        }

        for (it in located.x + 1 until width) {
            val element = grid[it][located.y]
            if (element is Bomb && !bombs.contains(element) && element.range > located.distanceBetween(element)) {
                bombs.add(element)
                minTimer = minOf(minTimer ?: element.timer, element.timer, timerToExplode(element, bombs) ?: element.timer)
            }
            if (element !is Floor) {
                break
            }
        }

        for (it in located.y - 1 downTo 0) {
            val element = grid[located.x][it]
            if (element is Bomb && !bombs.contains(element) && element.range > located.distanceBetween(element)) {
                bombs.add(element)
                minTimer = minOf(minTimer ?: element.timer, element.timer, timerToExplode(element, bombs) ?: element.timer)
            }
            if (element !is Floor) {
                break
            }
        }

        for (it in located.y + 1 until height) {
            val element = grid[located.x][it]
            if (element is Bomb && !bombs.contains(element) && element.range > located.distanceBetween(element)) {
                bombs.add(element)
                minTimer = minOf(minTimer ?: element.timer, element.timer, timerToExplode(element, bombs) ?: element.timer)
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

    fun getAccessiblePath(located: Located, pathsChecked: List<Located> = emptyList()): List<Located> {
        val paths = mutableListOf<Located>()

        val element = getGridElement(located)
        if (element is Floor || element is Item) {
            paths.add(element)
        } else if (located !is Player) {
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

    private fun addElement(element: Located) {
        grid[element.x][element.y] = element
    }

}