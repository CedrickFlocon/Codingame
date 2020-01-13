package org.neige.codingame.puzzle

import java.util.*

class Game(input: Scanner) {

    private var answer: String

    init {
        answer = input.nextLine()
    }

    fun answer(): String {
        return answer
    }
}