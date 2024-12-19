package org.neige.codingame.cellularena

@JvmInline
value class Coordinate private constructor(private val coordinate: Pair<Int, Int>) {

    constructor(x: Int, y: Int) : this(x to y)

    val x: Int
        get() = coordinate.first
    val y: Int
        get() = coordinate.second

    fun neighbor(): List<Coordinate> {
        return listOf(
            Coordinate(x + 1, y),
            Coordinate(x - 1, y),
            Coordinate(x, y + 1),
            Coordinate(x, y - 1),
        )
    }
}
