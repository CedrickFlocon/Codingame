package org.neige.codingame.totoro

data class Tree(
    val cellId: Int,
    val size: Int,
    val isMine: Boolean,
    val isDormant: Boolean
) {

    lateinit var cell: Cell

    val tomorrowSpookySize: Boolean
        get() = cell.tomorrowShadowSize > 0 && cell.tomorrowShadowSize >= size

}
