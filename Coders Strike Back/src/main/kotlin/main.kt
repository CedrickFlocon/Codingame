
import java.util.*
import java.io.*
import java.math.*

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    // game loop
    while (true) {
        val x = input.nextInt()
        val y = input.nextInt()
        val nextCheckpointX = input.nextInt() // x position of the next check point
        val nextCheckpointY = input.nextInt() // y position of the next check point
        val nextCheckpointDist = input.nextInt() // distance to the next checkpoint
        val nextCheckpointAngle = input.nextInt() // angle between your pod orientation and the direction of the next checkpoint
        val opponentX = input.nextInt()
        val opponentY = input.nextInt()

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");


        // You have to output the target position
        // followed by the power (0 <= thrust <= 100)
        // i.e.: "x y thrust"


        System.err.println("$nextCheckpointAngle")
        System.err.println("$nextCheckpointDist")

        if (nextCheckpointDist > 5000) {
            when (nextCheckpointAngle) {
                in -10..10 -> println("$nextCheckpointX $nextCheckpointY 100")
                in -20..20 -> println("$nextCheckpointX $nextCheckpointY 95")
                in -30..30 -> println("$nextCheckpointX $nextCheckpointY 90")
                in -50..40 -> println("$nextCheckpointX $nextCheckpointY 85")
                in -60..60 -> println("$nextCheckpointX $nextCheckpointY 80")
                in -70..70 -> println("$nextCheckpointX $nextCheckpointY 75")
                in -80..80 -> println("$nextCheckpointX $nextCheckpointY 70")
                else -> println("$nextCheckpointX $nextCheckpointY 65")
            }
        } else {
            when (nextCheckpointAngle) {
                in -5..5 -> println("$nextCheckpointX $nextCheckpointY 100")
                in -10..10 -> println("$nextCheckpointX $nextCheckpointY 95")
                in -15..15 -> println("$nextCheckpointX $nextCheckpointY 90")
                in -20..20 -> println("$nextCheckpointX $nextCheckpointY 85")
                in -25..25 -> println("$nextCheckpointX $nextCheckpointY 80")
                in -30..30 -> println("$nextCheckpointX $nextCheckpointY 75")
                in -35..35 -> println("$nextCheckpointX $nextCheckpointY 70")
                else -> println("$nextCheckpointX $nextCheckpointY 65")
            }
        }
    }

}