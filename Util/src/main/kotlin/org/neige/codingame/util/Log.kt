package org.neige.codingame.util

object Log {

    var loggable = true

    fun debug(message: Any?) {
        checkLoggable {
            System.err.println(message)
        }
    }

    fun <T> debug(board: Array<Array<T>>, stringify: (T) -> String) {
        checkLoggable {
            (board[0].indices).forEach { y ->
                var line = ""
                board.indices.forEach { x ->
                    line += stringify.invoke(board[x][y])
                }
                debug(line)
            }
        }
    }

    private fun checkLoggable(body: () -> Unit) {
        if (loggable) {
            body.invoke()
        }
    }

}