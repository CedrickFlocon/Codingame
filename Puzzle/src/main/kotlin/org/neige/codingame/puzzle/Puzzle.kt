package org.neige.codingame.puzzle

import java.util.Scanner

fun main() {
    val game = Game(Scanner(System.`in`))

    println(game.answer())
}