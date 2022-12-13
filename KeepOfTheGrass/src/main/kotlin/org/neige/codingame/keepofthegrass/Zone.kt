package org.neige.codingame.keepofthegrass

class Zone(
    val tiles: List<Tile> = mutableListOf(),
    val scrapAmount: Int = 0,
    val neutralTile: Int = 0,
    val myTile: Int = 0,
    val myUnit: Int = 0,
    val myRecycler: Int = 0,
    val opponentTile: Int = 0,
    val opponentUnit: Int = 0,
    val opponentRecycler: Int = 0
)
