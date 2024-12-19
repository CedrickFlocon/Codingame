package org.neige.codingame.cellularena

import java.util.*

//todo change kotlin version
//create test with saved input
//map for the board not easy to cast
//better rawData
//Game structure player, organ, ..

/**
 * Grow and multiply your organisms to end up larger than your opponent.
 **/
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)

    val width = input.nextInt() // columns in the game grid
    val height = input.nextInt() // rows in the game grid
    val board = mutableMapOf<Coordinate, Entity>()

    for (x in 0 until width) {
        for (y in 0 until height) {
            board[Coordinate(x, y)] = Entity.EMPTY
        }
    }

    while (true) {
        val entityCount = input.nextInt()
        for (i in 0 until entityCount) {
            val x = input.nextInt()
            val y = input.nextInt() // grid coordinate
            val type = input.next() // WALL, ROOT, BASIC, TENTACLE, HARVESTER, SPORER, A, B, C, D
            val owner = input.nextInt() // 1 if your organ, 0 if enemy organ, -1 if neither
            val organId = input.nextInt() // id of this entity if it's an organ, 0 otherwise
            val organDir = input.next() // N,E,S,W or X if not an organ
            val organParentId = input.nextInt()
            val organRootId = input.nextInt()

            board[Coordinate(x, y)] = Entity.RawData(type, owner, organId, organDir, organParentId, organRootId)
        }
        val myA = input.nextInt()
        val myB = input.nextInt()
        val myC = input.nextInt()
        val myD = input.nextInt() // your protein stock
        val oppA = input.nextInt()
        val oppB = input.nextInt()
        val oppC = input.nextInt()
        val oppD = input.nextInt() // opponent's protein stock
        val requiredActionsCount = input.nextInt() // your number of organisms, output an action for each one in any order
        for (i in 0 until requiredActionsCount) {
            val myOrgan = board.entries.filter { (it.value as? Entity.RawData)?.owner == 1 }
                .filter {
                    it.key.neighbor().any {
                        val tile = board[it] as? Entity.RawData ?: return@any true
                        if (tile.type == "A") return@any true
                        return@any false
                    }
                }

            if (myOrgan.isEmpty()) {
                println("WAIT")
                continue
            }

            myOrgan
                .firstOrNull { it.key.neighbor().any { (board[it] as? Entity.RawData)?.type == "A" } }
                ?.let {
                    val target = it.key.neighbor().first { (board[it] as? Entity.RawData)?.type == "A" }
                    println("GROW ${(it.value as Entity.RawData).organId} ${target.x} ${target.y} BASIC")
                }
                ?: run {
                    val organ = myOrgan.first()
                    val target = organ.key.neighbor().first { board[it] is Entity.EMPTY }
                    println("GROW ${(organ.value as Entity.RawData).organId} ${target.x} ${target.y} BASIC")
                }
        }
    }
}
