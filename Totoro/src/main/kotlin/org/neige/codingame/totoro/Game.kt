package org.neige.codingame.totoro

import org.neige.codingame.totoro.mcts.MonteCarloTreeSearch
import org.neige.codingame.totoro.state.State

class Game {

    companion object {
        const val MAX_DIRECTION = 6
        const val MAX_NUTRIENTS = 20
    }

    val mcts = MonteCarloTreeSearch()

    fun newState(newState: State) {
        val start = System.currentTimeMillis()
        val action = mcts.findNextMove(newState)
        val end = System.currentTimeMillis()

        action.play("${end - start} ms")
    }

}
