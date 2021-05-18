package org.neige.codingame.totoro

import org.neige.codingame.totoro.state.Board
import org.neige.codingame.totoro.state.Cell
import org.neige.codingame.totoro.state.Day
import org.neige.codingame.totoro.state.Player
import org.neige.codingame.totoro.state.State
import org.neige.codingame.totoro.state.Tree
import java.util.Scanner

fun main() {
    val input = Scanner(System.`in`)

    val cells = (0 until input.nextInt()).map {
        it to
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
    }.toMap()

    val board = Board(cells)

    val game = Game()

    while (true) {
        val day = input.nextInt() // the game lasts 24 days: 0-23
        val nutrients = input.nextInt() // the base score you gain from the next COMPLETE action

        val meSunPoints = input.nextInt() // your sun points
        val meScore = input.nextInt() // your current score
        val opponentSunPoints = input.nextInt() // opponent's sun points
        val opponentScore = input.nextInt() // opponent's score
        val opponentIsWaiting = input.nextInt() != 0 // whether your opponent is asleep until the next day

        val red = Player(Player.Who.RED, meSunPoints, meScore, false)
        val blue = Player(Player.Who.BLUE, opponentSunPoints, opponentScore, opponentIsWaiting)

        val trees = (0 until input.nextInt()).map {
            val cellIndex = input.nextInt() // location of this tree
            val size = input.nextInt() // size of this tree: 0-3
            val isMine = input.nextInt() != 0 // 1 if this is your tree
            val isDormant = input.nextInt() != 0 // 1 if this tree is dormant

            Tree(cells[cellIndex]!!, size, if (isMine) red else blue, isDormant)
        }

        val numberOfPossibleMoves = input.nextInt()
        if (input.hasNextLine()) {
            input.nextLine()
        }

        for (i in 0 until numberOfPossibleMoves) {
            val possibleMove = input.nextLine()
        }

        game.newState(State(board, trees, nutrients, Day(day), red, blue, null))
    }
}
