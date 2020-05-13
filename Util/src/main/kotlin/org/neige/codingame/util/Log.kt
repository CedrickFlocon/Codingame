package org.neige.codingame.util

object Log {

    private const val LOGGABLE = false

    fun debug(message: Any?) {
        if (LOGGABLE) {
            System.err.println(message)
        }
    }

    fun <T> debug(board: Array<Array<T>>, stringify: (T) -> String) {
        if (LOGGABLE) {
            (board[0].indices).forEach { y ->
                var line = ""
                board.indices.forEach { x ->
                    line += stringify.invoke(board[x][y])
                }
                debug(line)
            }
        }
    }

}