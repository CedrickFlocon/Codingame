package org.neige.codingame.totoro.mcts

import org.neige.codingame.totoro.Action
import org.neige.codingame.totoro.state.EndGame
import org.neige.codingame.totoro.state.State

sealed class Node(
    val state: State
) {

    var win = 0
        private set
    var playout = 0
        private set

    private var childrens: List<Child> = emptyList()

    fun randomChild() = state.nextPlayerAction.value.random().let { Child(state.play(it), it, this) }

    fun select(): Node {
        var node: Node = bestChild()
        while (node.childrens.isNotEmpty()) {
            node = node.bestChild()
        }

        return node
    }

    fun bestChild(): Node {
        if (childrens.isEmpty()) return this
        return childrens.maxBy { if (playout == 0) Int.MAX_VALUE else it.win / playout }!!
    }

    fun expand() {
        if (state.endGame != null) return
        childrens = state.nextPlayerAction.value.map {
            Child(state.play(it), it, this)
        }
    }

    fun simulate(): EndGame {
        var childNode = this
        while (childNode.state.endGame == null) {
            childNode = childNode.randomChild()
        }

        return childNode.state.endGame!!
    }

    fun backPropagation(endGame: EndGame) {
        var propagationNode: Node? = this
        while (propagationNode != null) {
            when (endGame) {
                EndGame.RED_WIN -> propagationNode.recordWin()
                EndGame.BLUE_WIN,
                EndGame.DRAW -> propagationNode.recordVisit()
            }
            propagationNode = (propagationNode as? Child)?.parent
        }
    }

    private fun recordWin() {
        win++
        playout++
    }

    private fun recordVisit() {
        playout++
    }

}

class Root(
    state: State
) : Node(state)

class Child(
    state: State,
    val action: Action,
    val parent: Node
) : Node(state)
