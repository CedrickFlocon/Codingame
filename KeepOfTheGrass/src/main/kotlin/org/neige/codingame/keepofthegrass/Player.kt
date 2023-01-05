package org.neige.codingame.keepofthegrass

import kotlin.math.max


class Player(
    val game: KeepOfTheGrass
) : Resettable, Computable, Debuggable {

    //Input
    var matter: Int = 0
    lateinit var base: Tile
    val opponent by lazy { game.opponent(this) }
    var end: End? = null

    //Compute
    var recyclerNumber: Int = 0
    var robotNumber: Int = 0
    var tileNumber: Int = 0
    var isolateTile = emptyList<Tile>()
    var isolatePointPotential = 0
    var accessibleTile = emptyList<Tile>()
    var accessiblePointPotential = 0

    val behaviour: Behaviour
        get() = when {
            end != null -> Behaviour.FINISH
            else -> Behaviour.EXPLORE
        }

    override fun reset() {
        recyclerNumber = 0
        robotNumber = 0
        tileNumber = 0
        isolateTile = emptyList()
        accessibleTile = emptyList()
    }

    override fun compute() {
        isolateTile = game.board.fields
            .filter { it.none { it.player == opponent } && it.any { it.player == this } }
            .flatten()
            .flatMap { it.tiles }
        isolatePointPotential = isolateTile.count { it.pointPotential }

        accessibleTile = game.board.fields
            .filter { it.any { it.player == this } }
            .flatten()
            .flatMap { it.tiles }
        accessiblePointPotential = accessibleTile.count { it.pointPotential }
    }

    fun actions(): List<Action> {
        return when (behaviour) {
            Behaviour.EXPLORE -> play()
            Behaviour.FINISH -> finish()
        }
    }

    /**
     * Explore
     * Move in direction of enemy base
     * Build to extract resource (don't split the field)
     * Spawn for better exploration
     */
    private fun play(): List<Action> {
        val builds = extractResource()
        val moves = explore(builds)
        val spawns = spawns(builds, moves)

        return (moves + spawns + builds)
    }

    /**
     * Finish explore
     */
    private fun finish(): List<Action> {
        return game.board
            .zones
            .filter { it.player == this && it.robot > 0 }
            .filter { it.borders.any { it.value.any { it.outside.owner == null } } }
            .flatMap { zone -> zone.tiles.filter { it.robot > 0 }.map { zone to it } }
            .mapNotNull { (zone, tile) ->
                zone.borders.flatMap { it.value.map { it.outside } }.firstOrNull { it.owner == null }?.let {
                    Action.Move(
                        number = 1,
                        from = tile,
                        to = it
                    )
                }
            } + game.board.fields
            .filter { it.any { it.player == this } && it.any { it.player == null } && it.none { it.robot > 0 } }
            .map { Action.Spawn(1, it.first { it.player == this }.tiles.first()) }
    }

    private fun remainingOperation(actions: List<Action>): Int {
        return max(matter - actions.sumOf { it.cost }, 0) / 10
    }

    private fun extractResource(): List<Action.Build> {
        val builds = mutableListOf<Action.Build>()

        game.board.fields
            .asSequence()
            .filter { field -> field.any { it.player == opponent } }
            .flatMap { field -> field.filter { it.player == this } }
            .forEach { zone ->
                val links = zone.borders
                    .flatMap { it.value }
                    .groupBy { it.inside }
                    .mapValues { it.value.map { it.outside } }

                if (links.count() <= remainingOperation(builds) && links.none { it.key.robot > 0 }) {
                    val recycled = game.board.recycled(links.keys.toList())
                    val opponentAccessiblePoint = opponent.accessibleTile.count { tile -> tile.pointPotential && recycled.none { it == tile } }
                    val isolatePoint = isolatePointPotential + zone.tiles.count { tile -> tile.pointPotential && recycled.none { it == tile } }
                    if (isolatePoint > opponentAccessiblePoint) {
                        links.forEach {
                            builds.add(Action.Build(it.key))
                        }
                    }
                }
            }

        game.board.fields
            .filter { field -> field.any { it.player == this } && field.any { it.player == opponent } }
            .forEach { field ->
                field
                    .filter { it.player == this }
                    .forEach { zone ->
                        zone.tiles.filter { it.owner == this && it.empty && it.recyclingPotential > 20 }
                            .sortedByDescending { it.recyclingPotential }
                            .asSequence()
                            .filter { tile -> !tile.inRangeOfRecycler && game.board.tilesInRange(tile).none { it.inRangeOfRecycler } }
                            .filter { tile -> builds.none { build -> game.board.tilesInRange(build.tile).any { it == tile } } }
                            .filter { tile ->
                                val recycled = game.board.recycled(builds.map { it.tile } + tile)
                                accessibleTile.count { tile -> tile.pointPotential && recycled.none { it == tile } } >
                                        opponent.isolateTile.count { tile -> tile.pointPotential && recycled.none { it == tile } }
                            }
                            .map { Action.Build(it) }
                            .takeWhile { remainingOperation(builds) > 0 }
                            .takeWhile { builds.count() + recyclerNumber <= 3 }
                            .forEach { builds.add(it) }
                    }
            }

        return builds
    }

    private fun explore(builds: List<Action.Build>): List<Action.Move> {
        val moves = mutableListOf<Action.Move>()
        game.board.zones
            .filter { it.player == this && it.robot > 0 }
            .flatMap { it.borders.values.flatten().filter { it.inside.robot > 0 } }
            .groupBy { it.inside }
            .mapValues { it.value.map { it.outside } }
            .toList()
            .sortedBy { it.second.size }
            .flatMap { link -> (0 until link.first.robot).map { link } }
            .forEach { (inside, outsides) ->
                val to = if (outsides.all { it.nextTurnGrass }) {
                    game.board.tilesInRange(inside).firstOrNull { !it.nextTurnGrass }
                } else {
                    outsides
                        .map { outside ->
                            Triple(
                                first = outside,
                                second = moves.filter { it.to == outside }.sumOf { it.number } - outside.robot,
                                third = outside.distanceTo(opponent.base)
                            )
                        }
                        .groupBy { it.second }
                        .minBy { it.key }
                        .value
                        .minByOrNull { it.third }
                        ?.first
                }

                to?.let {
                    moves.add(
                        Action.Move(
                            number = 1,
                            from = inside,
                            to = it
                        )
                    )
                }
            }

        game.board.fields
            .filter { it.any { it.player == this && it.robot > 0 } }
            .forEach { field ->
                field.filter { it.robot > 0 && it.player == this }
                    .forEach { zone ->
                        zone.tiles
                            .filter { it.robot > 0 }
                            .filter { tile -> zone.borders.values.flatten().none { it.inside == tile } }
                            .forEach { tile ->
                                val fieldTiles = field.flatMap { it.tiles }
                                val to = fieldTiles
                                    .filter { it.owner == opponent }
                                    .ifEmpty { fieldTiles.filter { it.owner == null } }
                                    .ifEmpty { fieldTiles }
                                    .minBy { it.distanceTo(tile) }
                                moves.add(
                                    Action.Move(
                                        number = 1,
                                        from = tile,
                                        to = to
                                    )
                                )
                            }
                    }
            }

        return moves
    }

    private fun spawns(builds: List<Action.Build>, moves: List<Action.Move>): List<Action.Spawn> {
        val spawns = mutableListOf<Action.Spawn>()
        game.board.fields
            .asSequence()
            .filter { field -> field.any { it.player == opponent } }
            .flatMap { field -> field.filter { it.player == this } }
            .flatMap { zone -> zone.borders.flatMap { it.value } }
            .groupBy { it.inside }
            .map { it.key }
            .filter { !it.nextTurnGrass }
            .sortedBy {
                it.distanceTo(opponent.base) + (game.board.tilesInRange(it) + it).sumOf { tileInRange -> tileInRange.robot + (2.takeIf { tileInRange.owner == this } ?: 0) }
            }
            .take(remainingOperation(builds))
            .forEach { spawns.add(Action.Spawn(1, it)) }

        return spawns
    }

    override fun debug() = """
        |$behaviour robot($robotNumber) & recycler($recyclerNumber)       
        |Accessible:${accessiblePointPotential} | Isolate:${isolatePointPotential}
    """.trimMargin()

    enum class Behaviour {
        EXPLORE,
        FINISH,
    }

}
