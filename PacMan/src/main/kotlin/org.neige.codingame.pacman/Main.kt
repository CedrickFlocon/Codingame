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
        var pac: Pac? = null
        val visiblePacCount = input.nextInt()
        for (i in 0 until visiblePacCount) {
            val pacId = input.nextInt()
            val mine = input.nextInt() != 0
            val x = input.nextInt()
            val y = input.nextInt()
            val typeId = input.next()
            val speedTurnsLeft = input.nextInt()
            val abilityCooldown = input.nextInt()

            if (mine) {
                pac = Pac(Coordinate(x, y))
            }
        }

        val pellets = Array(input.nextInt()) {
            val x = input.nextInt()
            val y = input.nextInt()
            val value = input.nextInt()

            Pellet(value, Coordinate(x, y))
        }

        Game(pac!!, pellets).play()
    }
}