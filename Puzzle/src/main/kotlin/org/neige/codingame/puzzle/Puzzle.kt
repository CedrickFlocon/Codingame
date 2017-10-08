package org.neige.codingame.puzzle

import java.util.*

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args : Array<String>) {
    val game = Game(Scanner(System.`in`))
    game.nextStep()

    println(game.answer())
}