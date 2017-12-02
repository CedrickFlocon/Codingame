package org.neige.codingame.hypersonic

import java.util.*


class Board(private val scanner: Scanner, private val width: Int, private val height: Int) {

    private val grid = Array(width, { i -> Array<Located>(height, { j -> Floor(i, j) }) })
    private val players = mutableMapOf<Int, Player>()

    fun nextTurn() {
        for (y in 0 until height) {
            scanner.next().forEachIndexed { x, c ->
                when (c) {
                    '.' -> addElement(Floor(x, y))
                    'X' -> addElement(Wall(x, y))
                    else -> addElement(Box(x, y))
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

            if (entityType == 0) {
                players.put(owner, Player(owner, x, y, param1, param2))
            } else if (entityType == 1) {
                addElement(Bomb(owner, x, y, param1, param2))
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

        for (x in 0 until width) {
            for (y in 0 until height) {
                if (grid[x][y] is Box && !willExplode(grid[x][y])) {
                    if (box == null || located.distanceBetween(grid[x][y]) < located.distanceBetween(box)) {
                        box = grid[x][y] as Box
                    }
                }
            }
        }
        return box
    }

    fun willExplode(located: Located): Boolean {
        if (grid[located.x][located.y] is Bomb) {
            return true
        }

        for (it in located.x - 1 downTo 0) {
            val element = grid[it][located.y]
            if (element is Bomb && element.range > located.distanceBetween(element))
                return true
            if (element !is Floor) {
                break
            }
        }

        for (it in located.x + 1 until width) {
            val element = grid[it][located.y]
            if (element is Bomb && element.range > located.distanceBetween(element))
                return true
            if (element !is Floor) {
                break
            }
        }

        for (it in located.y - 1 downTo 0) {
            val element = grid[located.x][it]
            if (element is Bomb && element.range > located.distanceBetween(element))
                return true
            if (element !is Floor) {
                break
            }
        }

        for (it in located.y + 1 until height) {
            val element = grid[located.x][it]
            if (element is Bomb && element.range > located.distanceBetween(element))
                return true
            if (element !is Floor) {
                break
            }
        }

        return false
    }

    private fun addElement(element: Located) {
        grid[element.x][element.y] = element
    }

}