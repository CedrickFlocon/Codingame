package org.neige.codingame.totoro

class Player {

    var sunPoints = 0
    var score = 0
    var isWaiting = false

    var trees = emptyList<Tree>()
        set(value) {
            field = value
            growCost[0] = 1 + trees.filter { it.size == 0 }.count()
            growCost[1] = 1 + trees.filter { it.size == 1 }.count()
            growCost[2] = 3 + trees.filter { it.size == 2 }.count()
            growCost[3] = 7 + trees.filter { it.size == 3 }.count()
            potentialSun = trees.filter { !it.tomorrowSpookySize }.sumBy { it.size }
        }
    val completeCost = 4
    var growCost = mutableMapOf(
        0 to 1,
        1 to 1,
        2 to 3,
        3 to 7
    )
    var potentialSun = 0

    fun growableTree(): List<Tree> {
        return trees
            .filter { it.size < 3 }
            .filter { !it.isDormant }
            .filter { sunPoints >= growCost[it.size + 1]!! }
    }

    fun completableTree(): List<Tree> {
        return trees
            .filter { it.size == 3 }
            .filter { !it.isDormant }
            .filter { sunPoints >= completeCost }
    }

}
