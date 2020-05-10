import org.neige.codingame.pacman.*
import java.util.*
import java.io.*
import java.math.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val width = input.nextInt()
    val height = input.nextInt()
    if (input.hasNextLine()) {
        input.nextLine()
    }
    for (i in 0 until height) {
        val row = input.nextLine()
    }

    while (true) {
        val myScore = input.nextInt()
        val opponentScore = input.nextInt()
        val pacs = Array(input.nextInt()) {
            val pacId = input.nextInt()
            val team = if (input.nextInt() != 0) Pac.Team.ALLY else Pac.Team.ENEMY
            val x = input.nextInt()
            val y = input.nextInt()
            val typeId = input.next()
            val speedTurnsLeft = input.nextInt()
            val abilityCooldown = input.nextInt()

            Pac(pacId, team, Coordinate(x, y))
        }

        val pellets = Array(input.nextInt()) {
            val x = input.nextInt()
            val y = input.nextInt()
            val value = input.nextInt()

            Pellet(value, Coordinate(x, y))
        }

        Game(pacs, pellets).play()
    }
}