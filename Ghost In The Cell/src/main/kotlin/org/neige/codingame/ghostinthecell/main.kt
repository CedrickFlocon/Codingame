package org.neige.codingame.ghostinthecell

import java.util.*


fun main() {
    var time: Long
    val input = Scanner(System.`in`)

    val factories = Array(input.nextInt()) { Factory(it) }
    val linkCount = input.nextInt()
    var distanceMax = 0
    for (i in 0 until linkCount) {
        val factory1 = factories[input.nextInt()]
        val factory2 = factories[input.nextInt()]
        val distance = input.nextInt()
        factory1.links.add(Link(factory1, factory2, distance))
        factory2.links.add(Link(factory2, factory1, distance))
        if (distance > distanceMax) {
            distanceMax = distance
        }
    }

    Link.DISTANCE_MAX = distanceMax
    val game = Game(input, factories)

    while (true) {
        time = System.currentTimeMillis()

        game.nextTurn()

        println(game.play().let {
            it + Waiting + Messaging("${System.currentTimeMillis() - time}")
        }.joinToString(";") { it.play() })
    }
}
