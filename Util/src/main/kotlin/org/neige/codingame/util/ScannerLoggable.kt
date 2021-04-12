package org.neige.codingame.util

import java.io.InputStream
import java.util.Scanner

class ScannerLoggable(input: InputStream) {

    private val scanner = Scanner(input)

    fun next(): String = scanner.next().also { Log.debug(it) }
    fun nextLine(): String = scanner.nextLine().also { Log.debug(it) }
    fun nextInt(): Int = scanner.nextInt().also { Log.debug(it) }

}