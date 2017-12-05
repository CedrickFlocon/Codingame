package org.neige.codingame.hypersonic


data class Item(override val x: Int, override val y: Int, val itemType: ItemType) : Located {

}