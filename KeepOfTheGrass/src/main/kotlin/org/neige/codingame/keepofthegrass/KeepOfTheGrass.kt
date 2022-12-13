package org.neige.codingame.keepofthegrass

import java.util.Scanner

/**
Small map seed :
seed=-1313270915990897700
Big map seed :
seed=-5029708052989020000
 **/
class KeepOfTheGrass(
    private val input: Scanner
) : Debuggable {

    val board: Board
    val player1 = Player(this)
    val player2 = Player(this)
    var turn = 0
    var time = 0L

    fun opponent(player: Player) = if (player == player1) player2 else player1

    init {
        board = Board(input.nextInt(), input.nextInt())
    }

    fun game() {
        while (true) {
            turn++
            board.reset()
            player1.reset()
            player2.reset()

            player1.matter = input.nextInt()
            player2.matter = input.nextInt()

            for (y in 0 until board.height) {
                for (x in 0 until board.width) {
                    val tile = board.grid[x][y]
                    tile.reset()
                    tile.scrapAmount = input.nextInt()
                    tile.owner = input.nextInt().let {
                        when (it) {
                            1 -> player1
                            0 -> player2
                            else -> null
                        }
                    }
                    tile.robot = input.nextInt()
                    tile.recycler = input.nextInt() == 1
                    tile.canBuild = input.nextInt() == 1
                    tile.canSpawn = input.nextInt() == 1
                    tile.inRangeOfRecycler = input.nextInt() == 1

                    board.scrapAmount += tile.scrapAmount

                    tile.owner?.let {
                        it.tileNumber += 1
                        it.robotNumber += tile.robot
                        it.recyclerNumber += if (tile.recycler) 1 else 0
                    }
                }
            }

            time = System.currentTimeMillis()

            //Region Compute
            board.compute()
            if (turn == 1) {
                val center = board.zones.filter { it.player != null }.map { it.tiles.first { it.robot == 0 } }.take(2)
                center[0].owner!!.base = center[0]
                center[1].owner!!.base = center[1]
            }

            player1.compute()
            player2.compute()

            when {
                player1.isolateTile > player2.accessibleTile -> End.WINNER to End.LOSER
                player2.isolateTile > player1.accessibleTile -> End.LOSER to End.WINNER
                board.fields.map { it.mapNotNull { it.player }.distinct() }.none { it.size > 1 } -> End.DRAW to End.DRAW
                else -> null to null
            }.also {
                player1.end = it.first
                player2.end = it.second
            }
            //EndRegion compute

            val playerAction = player1.actions()

            //Region Debug
            System.err.println(debug())
            val message = Action.Message("$turn - ${System.currentTimeMillis() - time}ms")
            //EndRegion

            (playerAction + message)
                .ifEmpty { listOf(Action.Wait) }
                .joinToString(";") { it.command() }
                .let { println(it) }
        }
    }

    override fun debug() = """
        |START $turn ${player1.end?.debug() ?: "Play"} 
        |Me ${player1.debug()}
        |Op ${player2.debug()}
        |
        |${board.debug()}
        |END   
    """.trimMargin()

}

enum class End : Debuggable {
    WINNER,
    LOSER,
    DRAW;

    override fun debug(): String {
        return when (this) {
            WINNER -> "Win"
            LOSER -> "Lose"
            DRAW -> "Draw"
        }
    }

}
