package org.neige.codingame.pacman

import org.neige.codingame.geometry.Coordinate
import java.util.Scanner

fun main() {
    val input = Scanner(System.`in`)

    val width = input.nextInt()
    val height = input.nextInt()
    if (input.hasNextLine()) {
        input.nextLine()
    }
    val rows = mutableListOf<String>()
    for (i in 0 until height) {
        rows.add(input.nextLine())
    }

    val grid = Array(width) { x ->
        Array(height) { y ->
            val coordinate = Coordinate(x, y)
            when (rows[y][x]) {
                '#' -> Wall(coordinate)
                ' ' -> Floor(coordinate)
                else -> throw IllegalArgumentException()
            }
        }
    }

    val board = Board(grid)
    val game = Game(board)

    while (true) {
        val score = mapOf(Pac.Team.ALLY to input.nextInt(), Pac.Team.ENEMY to input.nextInt())

        val pacs = Array(input.nextInt()) {
            val pacId = input.nextInt()
            val team = if (input.nextInt() != 0) Pac.Team.ALLY else Pac.Team.ENEMY
            val x = input.nextInt()
            val y = input.nextInt()
            val type = when (input.next()) {
                "ROCK" -> Pac.Type.ROCK
                "SCISSORS" -> Pac.Type.SCISSORS
                "PAPER" -> Pac.Type.PAPER
                else -> throw IllegalArgumentException()
            }
            val speedTurnsLeft = input.nextInt()
            val abilityCooldown = input.nextInt()

            Pac(pacId, team, type, speedTurnsLeft, abilityCooldown, Coordinate(x, y))
        }

        val pellets = Array(input.nextInt()) {
            val x = input.nextInt()
            val y = input.nextInt()
            val value = input.nextInt()

            Pellet(value, Coordinate(x, y))
        }

        game.nextTurn(pacs, pellets, score)
    }
}
