package org.neige.codingame.totoro.state

data class Tree(
    val cell: Cell,
    val size: Int,
    val owner: Player,
    val isDormant: Boolean
) {

    companion object {
        const val MAX_SIZE = 3
    }

}
