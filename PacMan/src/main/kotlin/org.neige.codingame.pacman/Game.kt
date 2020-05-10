import java.util.*
import java.io.*
import java.math.*

/**
 * Grab the pellets as fast as you can!
 **/
fun main(args : Array<String>) {
    val input = Scanner(System.`in`)
    val width = input.nextInt() // size of the grid
    val height = input.nextInt() // top left corner is (x=0, y=0)
    if (input.hasNextLine()) {
        input.nextLine()
    }
    for (i in 0 until height) {
        val row = input.nextLine() // one line of the grid: space " " is floor, pound "#" is wall
    }

    // game loop
    while (true) {
        val myScore = input.nextInt()
        val opponentScore = input.nextInt()
        val visiblePacCount = input.nextInt() // all your pacs and enemy pacs in sight
        for (i in 0 until visiblePacCount) {
            val pacId = input.nextInt() // pac number (unique within a team)
            val mine = input.nextInt() != 0 // true if this pac is yours
            val x = input.nextInt() // position in the grid
            val y = input.nextInt() // position in the grid
            val typeId = input.next() // unused in wood leagues
            val speedTurnsLeft = input.nextInt() // unused in wood leagues
            val abilityCooldown = input.nextInt() // unused in wood leagues
        }
        val visiblePelletCount = input.nextInt() // all pellets in sight
        for (i in 0 until visiblePelletCount) {
            val x = input.nextInt()
            val y = input.nextInt()
            val value = input.nextInt() // amount of points this pellet is worth
        }

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");

        println("MOVE 0 15 10") // MOVE <pacId> <x> <y>
    }
}