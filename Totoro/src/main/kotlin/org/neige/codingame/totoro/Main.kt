package org.neige.codingame.totoro

import java.util.Scanner

fun main() {
    val input = Scanner(System.`in`)
    val numberOfCells = input.nextInt() // 37

    val cells = (0 until numberOfCells).map {
        val index = input.nextInt() // 0 is the center cell, the next cells spiral outwards
        val richness = input.nextInt() // 0 if the cell is unusable, 1-3 for usable cells
        val neigh0 = input.nextInt() // the index of the neighbouring cell for each direction
        val neigh1 = input.nextInt()
        val neigh2 = input.nextInt()
        val neigh3 = input.nextInt()
        val neigh4 = input.nextInt()
        val neigh5 = input.nextInt()

        Cell(index, richness)
    }

    // game loop
    while (true) {
        val day = input.nextInt() // the game lasts 24 days: 0-23
        val nutrients = input.nextInt() // the base score you gain from the next COMPLETE action
        val sun = input.nextInt() // your sun points
        val score = input.nextInt() // your current score
        val oppSun = input.nextInt() // opponent's sun points
        val oppScore = input.nextInt() // opponent's score
        val oppIsWaiting = input.nextInt() != 0 // whether your opponent is asleep until the next day
        val numberOfTrees = input.nextInt() // the current amount of trees

        val trees = (0 until numberOfTrees).map {
            val cellIndex = input.nextInt() // location of this tree
            val size = input.nextInt() // size of this tree: 0-3
            val isMine = input.nextInt() != 0 // 1 if this is your tree
            val isDormant = input.nextInt() != 0 // 1 if this tree is dormant

            Tree(cellIndex, size, isMine, isDormant)
        }

        val numberOfPossibleMoves = input.nextInt()
        if (input.hasNextLine()) {
            input.nextLine()
        }
        for (i in 0 until numberOfPossibleMoves) {
            val possibleMove = input.nextLine()
        }

        (trees
            .filter { it.isMine && it.size == 3 }
            .firstOrNull()
            ?.let { Complete(it.cellId) }
            ?: trees
                .filter { it.isMine }
                .firstOrNull()
                ?.let { Grow(it.cellId) }
            ?: Wait()).play()
    }
}