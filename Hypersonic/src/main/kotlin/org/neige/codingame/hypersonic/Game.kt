package org.neige.codingame.hypersonic


class Game(private val board: Board, private val playerId: Int) {

    fun nextTurn() {
        board.nextTurn()

        board.getPlayer(playerId).play(board).play()
    }

}