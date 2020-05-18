package org.neige.codingame.util

import java.io.InputStream
import java.util.Scanner

class ScannerLoggable(input: InputStream) {

    private val scanner = Scanner(input)

    fun nextLine(): String {
        return scanner.nextLine().also { Log.debug(it) }
    }

}