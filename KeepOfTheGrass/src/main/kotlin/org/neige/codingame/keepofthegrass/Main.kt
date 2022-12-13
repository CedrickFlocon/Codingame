package org.neige.codingame.keepofthegrass

import java.util.Scanner

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val width = input.nextInt()
    val height = input.nextInt()

    // game loop
    while (true) {
        val myMatter = input.nextInt()
        val oppMatter = input.nextInt()
        for (i in 0 until height) {
            for (j in 0 until width) {
                val scrapAmount = input.nextInt()
                val owner = input.nextInt() // 1 = me, 0 = foe, -1 = neutral
                val units = input.nextInt()
                val recycler = input.nextInt()
                val canBuild = input.nextInt()
                val canSpawn = input.nextInt()
                val inRangeOfRecycler = input.nextInt()
            }
        }

        // Write an action using println()
        // To debug: System.err.println("Debug messages...");

        println("WAIT")
    }
}
