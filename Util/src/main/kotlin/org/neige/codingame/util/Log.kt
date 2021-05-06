package org.neige.codingame.util

object Log {

    var loggable = true

    fun debug(message: Any?) {
        { System.err.println(if (message is Debuggable) message.debug() else message) }
            .takeIf { loggable }
            ?.invoke()
    }

    fun <T> debug(board: Array<Array<T>>, stringify: (T) -> String) {
        {
            (board[0].indices).forEach { y ->
                var line = ""
                board.indices.forEach { x ->
                    line += stringify.invoke(board[x][y])
                }
                debug(line)
            }
        }.takeIf { loggable }?.invoke()
    }

}