package org.neige.codingame.hypersonic

import java.util.*


class Board(private val scanner: Scanner, private val width: Int, private val height: Int) {

    private val grid = Array(width, { i -> Array<Located>(height, { j -> Floor(i, j) }) })
    private val players = mutableListOf<Player>()

    fun nextTurn() {
        clear()

        for (y in 0 until height) {
            scanner.next().forEachIndexed { x, c ->
                if (c != '.') {
                    addBox(Box(x, y))
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
                players.add(Player(owner, x, y))
            } else if (entityType == 1) {
                grid[x][y] = Bomb(owner, x, y)
            }
        }

    }

    fun getPlayer(playerId: Int): Player {
        return players.find { it.id == playerId }!!
    }

    fun getGridElement(located: Located): Located {
        return grid[located.x][located.y]
    }

    fun getClosetedBox(located: Located): Box? {
        var box: Box? = null

        for (x in 0 until width) {
            for (y in 0 until height) {
                if (grid[x][y] is Box) {
                    if (box == null || located.distanceBetween(grid[x][y]) < located.distanceBetween(box)) {
                        box = grid[x][y] as Box
                    }
                }
            }
        }
        return box
    }

    private fun addBox(box: Box) {
        grid[box.x][box.y] = box
    }

    private fun clear() {
        players.clear()

        for (x in 0 until width) {
            for (y in 0 until height) {
                grid[x][y] = Floor(x, y)
            }
        }
    }

}