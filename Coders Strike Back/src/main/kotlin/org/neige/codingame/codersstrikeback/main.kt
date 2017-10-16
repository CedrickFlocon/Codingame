package org.neige.codingame.codersstrikeback

import org.neige.codingame.codersstrikeback.pod.Hunter
import org.neige.codingame.codersstrikeback.pod.Opponent
import org.neige.codingame.codersstrikeback.pod.Runner
import java.util.*

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    val laps = input.nextInt()
    val checkpoints = (0 until input.nextInt()).map { Checkpoint(Coordinate(input.nextInt().toDouble(), input.nextInt().toDouble())) }
    val game = Game(input, laps, checkpoints)
    val pod1 = Runner(game, Coordinate(input.nextDouble(), input.nextDouble()), Scalar(input.nextDouble(), input.nextDouble()), input.nextInt(), checkpoints[input.nextInt()])
    val pod2 = Hunter(game, Coordinate(input.nextDouble(), input.nextDouble()), Scalar(input.nextDouble(), input.nextDouble()), input.nextInt(), checkpoints[input.nextInt()])
    val opponentPod1 = Opponent(game, Coordinate(input.nextDouble(), input.nextDouble()), Scalar(input.nextDouble(), input.nextDouble()), input.nextInt(), checkpoints[input.nextInt()])
    val opponentPod2 = Opponent(game, Coordinate(input.nextDouble(), input.nextDouble()), Scalar(input.nextDouble(), input.nextDouble()), input.nextInt(), checkpoints[input.nextInt()])

    game.init(listOf(pod1, pod2), listOf(opponentPod1, opponentPod2))

    do {
        game.play().forEach { println(it) }
    } while (game.nextStep())

}