package org.neige.codingame.totoro

data class Tree(
    val cellId: Int,
    val size: Int,
    val owner: Player,
    val isDormant: Boolean
) {

    lateinit var cell: Cell

    val tomorrowSpookySize: Boolean
        get() = cell.tomorrowShadowSize > 0 && cell.tomorrowShadowSize >= size


    override fun toString(): String {
        return "Tree  : cellId $cellId $tomorrowSpookySize"
    }
}
