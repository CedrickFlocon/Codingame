package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector
import java.util.Scanner

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val base = Coordinate(input.nextInt(), input.nextInt()) // The corner of the map representing your base
    val heroesPerPlayer = input.nextInt() // Always 3

    // game loop
    while (true) {
        val spiders = mutableListOf<Spider>()
        val myHero = mutableListOf<Hero>()
        val opponentHero = mutableListOf<Hero>()

        for (i in 0 until 2) {
            val health = input.nextInt() // Each player's base health
            val mana = input.nextInt() // Ignore in the first league; Spend ten mana to cast a spell
        }
        val entityCount = input.nextInt() // Amount of heros and monsters you can see
        for (i in 0 until entityCount) {
            val id = input.nextInt() // Unique identifier
            val type = input.nextInt() // 0=monster, 1=your hero, 2=opponent hero
            val coordinate = Coordinate(input.nextInt(), input.nextInt()) // Position of this entity
            val shieldLife = input.nextInt() // Ignore for this league; Count down until shield spell fades
            val isControlled = input.nextInt() // Ignore for this league; Equals 1 when this entity is under a control spell
            val health = input.nextInt() // Remaining health of this monster
            val v = Vector(input.nextInt(), input.nextInt()) // Trajectory of this monster
            val nearBase = input.nextInt() == 1 // 0=monster with no target yet, 1=monster targeting a base
            val threatFor = when (input.nextInt()) { // Given this monster's trajectory, is it a threat to 1=your base, 2=your opponent's base, 0=neither
                -1, 0 -> null
                1 -> Player.ME
                2 -> Player.OPPONENT
                else -> throw Exception("Threat For $")
            }

            when (type) {
                0 -> spiders.add(Spider(id, coordinate, health, v, nearBase, threatFor))
                1 -> myHero.add(Hero(id, coordinate))
                2 -> opponentHero.add(Hero(id, coordinate))
                else -> throw Exception()
            }
        }
        for (i in 0 until heroesPerPlayer) {
            spiders
                .find { it.threatFor == Player.ME }
                ?.let { Action.move(it.coordinate) }
                ?: Action.halt()
        }
    }

}
