package org.neige.codingame.spiderattack

import org.neige.codingame.geometry.Coordinate
import org.neige.codingame.geometry.Vector
import java.util.Scanner

class Game(input: Scanner) {

    init {
        Board.myBase = Base(
            Coordinate(input.nextInt(), input.nextInt()), // The corner of the map representing your base
            Player.ME
        )

        Board.opponentBase = Base(
            if (Board.myBase.coordinate.x == 0) Coordinate(17630, 9000) else Coordinate(0, 0),
            Player.OPPONENT
        )

        val heroesPerPlayer = input.nextInt() // Always 3

        // game loop
        while (true) {
            val spiders = mutableListOf<Spider>()
            val myHero = mutableListOf<Hero>()
            val opponentHero = mutableListOf<Hero>()

            Board.myBase.health = input.nextInt()
            Board.myBase.mana = input.nextInt()

            Board.opponentBase.health = input.nextInt()
            Board.opponentBase.mana = input.nextInt()

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
                    else -> throw Exception("Threat For")
                }

                when (type) {
                    0 -> spiders.add(Spider(id, coordinate, health, v, nearBase, threatFor))
                    1 -> myHero.add(Hero(id, coordinate, Player.ME))
                    2 -> opponentHero.add(Hero(id, coordinate, Player.OPPONENT))
                    else -> throw Exception()
                }
            }

            myHero.forEach {
                it.spiders = spiders
                it.opponentsHero = opponentHero
                it.otherHero = (myHero - it).toMutableList()
            }

            opponentHero.forEach {
                it.spiders = spiders
                it.opponentsHero = myHero
                it.otherHero = (opponentHero - it).toMutableList()
            }

            myHero.forEach {
                Board.myBase.play(it.action())
            }
        }
    }

}
