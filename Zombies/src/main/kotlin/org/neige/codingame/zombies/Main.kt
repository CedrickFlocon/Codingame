package org.neige.codingame.zombies

import org.neige.codingame.geometry.Coordinate
import java.util.Scanner
import kotlin.math.roundToLong

/**
 * Save humans, destroy zombies!
 **/
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    // game loop
    while (true) {
        val x = input.nextInt()
        val y = input.nextInt()

        val ash = Coordinate(x, y)

        val humans = mutableMapOf<Int, Coordinate>()
        humans[-1] = ash
        val humanCount = input.nextInt()
        for (i in 0 until humanCount) {
            val humanId = input.nextInt()
            val humanX = input.nextInt()
            val humanY = input.nextInt()
            humans[humanId] = Coordinate(humanX, humanY)
        }

        val zombieCount = input.nextInt()
        val zombies = mutableMapOf<Int, Coordinate>()
        for (i in 0 until zombieCount) {
            val zombieId = input.nextInt()
            val zombieX = input.nextInt()
            val zombieY = input.nextInt()
            val zombieXNext = input.nextInt()
            val zombieYNext = input.nextInt()
            zombies[zombieId] = Coordinate(zombieX, zombieY)
        }

        val distanceZombieHuman = zombies.flatMap { zombie -> humans.map { human -> Triple(zombie, human, zombie.value.distanceFrom(human.value).roundToLong()) } }
        val targetedZombieHuman = distanceZombieHuman.groupBy { it.first }.mapNotNull { it.value.minBy { it.third } }

        val savableHumans = targetedZombieHuman
            .filter { it.second.key != -1 }
            .groupBy { it.second.key }.mapNotNull { it.value.minBy { it.third } }
            .filter { (it.second.value.distanceFrom(ash) - 1600) / 1000 - Math.ceil(it.third.toDouble() / 400) < 0 }
            .sortedBy { it.third }

        println(savableHumans.firstOrNull()?.second?.value?.print() ?: zombies.map { it.value }.minBy { it.distanceFrom(ash) }!!.print())
    }
}

fun Coordinate.print(): String {
    return "$x $y"
}