package org.neige.codingame.util

object Log {

    fun debug(message: String?) {
        System.err.println(message)
    }

    fun debug(message: Char?) {
        System.err.println(message)
    }

    fun debug(message: Int?) {
        System.err.println(message)
    }

    fun <T> debug(board: Array<Array<T>>, stringify: (T) -> String) {
        (board[0].indices).forEach { y ->
            var line = ""
            board.indices.forEach { x ->
                line += stringify.invoke(board[x][y])
            }
            debug(line)
        }
    }

}