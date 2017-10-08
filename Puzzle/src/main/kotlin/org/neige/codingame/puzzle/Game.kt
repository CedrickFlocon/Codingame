package org.neige.codingame.puzzle

import java.util.*

class Game(private val input: Scanner) {

    var answer: String = ""

    fun nextStep() {
        answer = input.nextLine()
        System.err.println(answer)
    }

    fun answer(): String {
        return answer
    }
}