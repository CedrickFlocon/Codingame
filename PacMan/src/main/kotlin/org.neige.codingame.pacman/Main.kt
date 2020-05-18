package org.neige.codingame.pacman

import java.util.Scanner

fun main() {
    val input = Scanner(System.`in`)

    val board = Board(Board.readGrid(input))

    val game = Game(board)

    while (true) {
        val score = mapOf(Pac.Team.ALLY to input.nextInt(), Pac.Team.ENEMY to input.nextInt())

        val pacs = Array(input.nextInt()) { Pac.readPac(input) }.toMutableList()
        val pellets = Array(input.nextInt()) { Pellet.readPellet(input) }.toMutableList()

        game.nextTurn(pacs, pellets, score)
    }
}
