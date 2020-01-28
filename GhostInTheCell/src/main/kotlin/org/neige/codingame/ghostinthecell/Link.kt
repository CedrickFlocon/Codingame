package org.neige.codingame.ghostinthecell

data class Link(
        val from: Factory,
        val to: Factory,
        val distance: Int
) {

    companion object {

        var DISTANCE_MAX = 0

    }

    override fun toString(): String {
        return "Link : from:${from.id} to:${to.id} distance:$distance"
    }

}
