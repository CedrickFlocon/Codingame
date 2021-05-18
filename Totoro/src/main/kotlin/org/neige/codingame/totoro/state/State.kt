package org.neige.codingame.totoro.state

import org.neige.codingame.totoro.Action
import org.neige.codingame.totoro.Complete
import org.neige.codingame.totoro.Grow
import org.neige.codingame.totoro.Seed
import org.neige.codingame.totoro.Wait
import kotlin.math.max


data class State(
    val board: Board,
    val trees: List<Tree>,
    val nutrients: Int,
    val day: Day,
    val red: Player,
    val blue: Player,
    val redAction: Action? = null,
    val endGame: EndGame? = null
) {

    val redTrees = lazy { trees.filter { it.owner.who == Player.Who.RED } }
    val blueTrees = lazy { trees.filter { it.owner.who == Player.Who.BLUE } }

    val nextPlayerAction = lazy {
        possibleAction(if (redAction == null) Player.Who.RED else Player.Who.BLUE).filter {
            filterPredicate(it)
        }
    }

    private val filterPredicate = { action: Action ->
        val playerTree = if (action.player.who == Player.Who.RED) redTrees else blueTrees

        when (action) {
            is Complete -> day.day > 10
            is Grow -> day.countDown > 2 - action.tree.size && action.extraCost < 6
            is Seed -> day.day !in 0..1 && day.day !in 19..22 && action.extraCost == 0 && playerTree.value.flatMap { it.cell.neighborsId }.none { it == action.cell.id }
            is Wait -> true
        }
    }

    fun play(action: Action): State {
        return if (action.player.who == Player.Who.RED) {
            this.copy(redAction = action)
        } else {
            endTurn(action)
        }
    }

    private fun endTurn(action: Action): State {
        checkNotNull(redAction)

        if (redAction is Wait && action is Wait) {
            return endDay()
        }

        if (redAction is Seed && action is Seed && redAction.cell.id == action.cell.id) {
            return this.copy(
                trees = trees.filter { it.cell.id != action.tree.cell.id && it.cell.id == action.cell.id } +
                        action.tree.copy(isDormant = true) + redAction.tree.copy(isDormant = true)
            )
        }

        val user = { userAction: Action ->
            when (userAction) {
                is Complete -> userAction.player.copy(
                    sunPoints = userAction.player.sunPoints - userAction.sunCost,
                    score = userAction.player.score + this.nutrients + userAction.tree.cell.richnessScore
                )
                is Grow -> userAction.player.copy(
                    sunPoints = userAction.player.sunPoints - userAction.sunCost
                )
                is Seed -> userAction.player.copy(
                    sunPoints = userAction.player.sunPoints - userAction.sunCost
                )
                is Wait -> userAction.player.copy(isWaiting = true)
            }
        }
        val newRed = user(redAction)
        val newBlue = user(action)

        val newNutrients = this.nutrients - if (redAction is Complete) 1 else 0 - if (action is Complete) 1 else 0

        val filterTree = { userAction: Action ->
            when (userAction) {
                is Complete -> { tree: Tree -> tree.cell.id != userAction.tree.cell.id }
                is Grow -> { tree: Tree -> tree.cell.id != userAction.tree.cell.id }
                is Seed -> { tree: Tree -> tree.cell.id != userAction.tree.cell.id }
                else -> { _: Tree -> true }
            }
        }
        val addTree = { userAction: Action ->
            when (userAction) {
                is Grow -> listOf(userAction.tree.copy(size = userAction.tree.size + 1, isDormant = true))
                is Seed -> listOf(userAction.tree.copy(isDormant = true), Tree(userAction.cell, 0, userAction.player, true))
                else -> emptyList()
            }
        }

        val newTrees = this.trees
            .filter { filterTree(redAction).invoke(it) }
            .filter { filterTree(action).invoke(it) } +
                addTree(redAction) + addTree(action)

        return this.copy(
            trees = newTrees,
            nutrients = newNutrients,
            red = newRed,
            blue = newBlue,
            redAction = null
        )
    }

    private fun endDay(): State {
        return if (day.day == 23) {
            endGame()
        } else {
            val tomorrow = day.copy(day = day.day + 1)
            val sunDirection = day.sunDirectionIn(tomorrow.day)
            val cellShadowed: MutableMap<Int, Int> = mutableMapOf()

            board.cellsNeighborsSunDirection
                .filter { it.value[day.oppositeSunDirectionIn(tomorrow.day)]?.firstOrNull() == null }
                .map { it.key }
                .forEach { borderCellId ->
                    var originalCellId: Int? = borderCellId

                    do {
                        trees.find { it.cell.id == originalCellId }?.let { tree ->
                            var shadowCellId = tree.cell.id
                            for (i in tree.size downTo 1) {
                                shadowCellId = board.cells.get(shadowCellId)?.neighborsId?.get(sunDirection) ?: break
                                cellShadowed[shadowCellId] = max(cellShadowed[shadowCellId] ?: 0, tree.size)
                            }
                        }
                        originalCellId = originalCellId?.let { board.cells.get(it)?.neighborsId?.get(sunDirection) }

                    } while (originalCellId != null)
                }

            val newRed = red.copy(
                isWaiting = false,
                sunPoints = red.sunPoints + redTrees.value
                    .filter { tree -> cellShadowed.none { it.key == tree.cell.id && tree.size == it.value } }
                    .sumBy { it.size })

            val newBlue = blue.copy(
                isWaiting = false,
                sunPoints = blue.sunPoints + blueTrees.value
                    .filter { tree -> cellShadowed.none { it.key == tree.cell.id } }
                    .sumBy { it.size })

            return this.copy(
                trees = trees.map { it.copy(isDormant = false) },
                day = tomorrow,
                red = newRed,
                blue = newBlue,
                redAction = null
            )
        }
    }

    private fun endGame(): State {
        val endGame = when {
            red.score > blue.score -> EndGame.RED_WIN
            blue.score > red.score -> EndGame.BLUE_WIN
            redTrees.value.count() > blueTrees.value.count() -> EndGame.RED_WIN
            blueTrees.value.count() > redTrees.value.count() -> EndGame.BLUE_WIN
            else -> EndGame.DRAW
        }

        return this.copy(
            endGame = endGame
        )
    }

    private fun possibleAction(who: Player.Who): List<Action> {
        val player = if (who == Player.Who.RED) red else blue

        if (player.isWaiting) return listOf(Wait(player))

        val playerTrees = trees.filter { it.owner.who == who }
        val playerActiveTrees = playerTrees.filter { !it.isDormant }

        val growCost = mutableMapOf(
            0 to playerTrees.filter { it.size == 0 }.count(),
            1 to Grow.BASE_COST[1]!! + playerTrees.filter { it.size == 1 }.count(),
            2 to Grow.BASE_COST[2]!! + playerTrees.filter { it.size == 2 }.count(),
            3 to Grow.BASE_COST[3]!! + playerTrees.filter { it.size == 3 }.count()
        )

        return playerActiveTrees
            .filter { it.size < Tree.MAX_SIZE && red.sunPoints >= growCost[it.size + 1]!! }
            .map { Grow(player, growCost[it.size + 1]!!, it) } +

                (playerActiveTrees.takeIf { red.sunPoints >= Complete.COMPLETE_COST } ?: emptyList())
                    .filter { it.size == Tree.MAX_SIZE }
                    .map { Complete(player, it) } +

                (playerActiveTrees.takeIf { red.sunPoints >= growCost[0]!! } ?: emptyList())
                    .filter { it.size > 0 }
                    .flatMap { tree ->
                        board.cellsNeighborsSunDirection[tree.cell.id]!!
                            .flatMap { it.value.take(tree.size) }
                            .filter { (cell, _) -> cell.richness > 0 && trees.none { it.cell.id == cell.id } }
                            .map { tree to it }
                    }
                    .map { Seed(player, growCost[0]!!, it.first, it.second.first, it.second.second) } +

                Wait(player)
    }

}
