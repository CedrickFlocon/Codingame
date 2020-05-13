package org.neige.codingame.pacman


class Game(private val board: Board) {

    fun nextTurn(pacs: Array<Pac>, pellets: Array<Pellet>, score: Map<Pac.Team, Int>) {
        board.updateInfo(pacs, pellets)

        board.debug()

        println(
                pacs.filter { it.team == Pac.Team.ALLY }
                        .mapNotNull { it.action(board) }
                        .joinToString("| ")
        )
    }

}
