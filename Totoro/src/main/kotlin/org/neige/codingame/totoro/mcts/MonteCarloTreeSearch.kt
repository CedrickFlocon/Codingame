package org.neige.codingame.totoro.mcts

import org.neige.codingame.totoro.Action
import org.neige.codingame.totoro.state.State

class MonteCarloTreeSearch {

    fun findNextMove(newState: State): Action {
        val rootNode = Root(newState)
        val end = System.currentTimeMillis() + 90
        while (System.currentTimeMillis() < end) {
            // Phase 1 - Selection
            val node = rootNode.select()

            // Phase 2 - Expansion
            node.expand()

            // Phase 3 - Simulation
            val nodeToExplore = node.randomChild()
            val result = nodeToExplore.simulate()

            // Phase 4 - Backpropagation
            nodeToExplore.backPropagation(result)
        }
        return (rootNode.bestChild() as Child).action
    }

}
