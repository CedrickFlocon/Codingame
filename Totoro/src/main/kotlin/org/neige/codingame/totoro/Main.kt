package org.neige.codingame.totoro

import java.util.Scanner

fun main() {
    val input = Scanner(System.`in`)

    val game = Game(
        Board(
            (0 until input.nextInt()).map {
                Cell(
                    input.nextInt(),
                    input.nextInt(),
                    listOf(
                        input.nextInt().takeIf { it != -1 },
                        input.nextInt().takeIf { it != -1 },
                        input.nextInt().takeIf { it != -1 },
                        input.nextInt().takeIf { it != -1 },
                        input.nextInt().takeIf { it != -1 },
                        input.nextInt().takeIf { it != -1 }
                    )
                )
            }
        ),
        Player(),
        Player()
    )

    while (true) {
        val day = input.nextInt() // the game lasts 24 days: 0-23
        game.nutrients = input.nextInt() // the base score you gain from the next COMPLETE action
        game.me.sunPoints = input.nextInt() // your sun points
        game.me.score = input.nextInt() // your current score
        game.opponent.sunPoints = input.nextInt() // opponent's sun points
        game.opponent.score = input.nextInt() // opponent's score
        game.opponent.isWaiting = input.nextInt() != 0 // whether your opponent is asleep until the next day

        val trees = (0 until input.nextInt()).map {
            val cellIndex = input.nextInt() // location of this tree
            val size = input.nextInt() // size of this tree: 0-3
            val isMine = input.nextInt() != 0 // 1 if this is your tree
            val isDormant = input.nextInt() != 0 // 1 if this tree is dormant

            Tree(cellIndex, size, isMine, isDormant)
        }

        val numberOfPossibleMoves = input.nextInt()
        if (input.hasNextLine()) {
            input.nextLine()
        }
        for (i in 0 until numberOfPossibleMoves) {
            val possibleMove = input.nextLine()
        }

        game.board.nextTurn(trees, day)
        game.day = day

        game.nextTurn()
    }
}