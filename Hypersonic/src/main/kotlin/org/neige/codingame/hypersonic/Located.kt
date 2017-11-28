package org.neige.codingame.hypersonic


interface Located {

    val x: Int
    val y: Int

    fun distanceBetween(located: Located): Int {
        return Math.abs(located.x - x) + Math.abs(located.y - y)
    }

    fun checkNeighbour(coordinate: Located): Boolean {
        return (coordinate.y == y && coordinate.x in x - 1..x + 1).xor((coordinate.x == x && coordinate.y in y - 1..y + 1))
    }

}