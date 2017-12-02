package org.neige.codingame.hypersonic


data class Bomb(override val id: Int, override val x: Int, override val y: Int, val timer: Int, val range: Int) : Owner, Located {
}