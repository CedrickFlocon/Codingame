package org.neige.codingame.hypersonic


interface Located {

    val x: Int
    val y: Int

    fun distanceBetween(located: Located): Int {
        return Math.abs(located.x - x) + Math.abs(located.y - y)
    }

    fun checkNeighbour(located: Located): Boolean {
        return (located.y == y && located.x in x - 1..x + 1).xor((located.x == x && located.y in y - 1..y + 1))
    }

    fun sameLocated(located: Located): Boolean {
        return located.y == y && located.x == x
    }

}