package org.neige.codingame.keepofthegrass

import java.util.Scanner

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val board = Board(input.nextInt(), input.nextInt())
    val me = Player(board)
    val opponent = Player(board)

    while (true) {
        me.matter = input.nextInt()
        opponent.matter = input.nextInt()

        val actions = mutableListOf<String>()
        for (y in 0 until board.height) {
            for (x in 0 until board.width) {
                val tile = board.grid[x][y]
                tile.scrapAmount = input.nextInt()
                tile.owner = input.nextInt().let {
                    when (it) {
                        1 -> Owner.ME
                        0 -> Owner.OPPONENT
                        else -> null
                    }
                }
                tile.units = input.nextInt()
                tile.recycler = input.nextInt() == 1
                tile.canBuild = input.nextInt() == 1
                tile.canSpawn = input.nextInt() == 1
                tile.inRangeOfRecycler = input.nextInt() == 1
            }
        }

        board.compute()

        me.play()
    }
}
