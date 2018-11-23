package org.neige.codingame.hypersonic

import java.util.*


fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val board = Board(input, input.nextInt(), input.nextInt())
    val game = Game(board, input.nextInt())

    while (true) {
        game.nextTurn()
    }
}