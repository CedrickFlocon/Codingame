package org.neige.codingame.pacman


class Game(private val board: Board) {

    var turnNumber: Int = 0
        private set

    fun nextTurn(pacs: List<Pac>, pellets: List<Pellet>, score: Map<Pac.Team, Int>) {
        board.updateInfo(pacs, pellets, score)
        board.debug()

        val ally = board.alivePacs.filter { it.team == Pac.Team.ALLY }
        ally.forEach { it.buildCommand(board) }

        val pacsMove = ally.mapNotNull { it.command }.filterIsInstance<Move>()
        val distinctMove = pacsMove.distinctBy { it.path.firstOrNull() }.map { it.pac }
        pacsMove.filter { move -> distinctMove.none { it == move.pac } }.forEach { it.pac.cancelCommand() }

        println(ally.mapNotNull { it.command }.joinToString("| "))

        turnNumber++
    }

}
