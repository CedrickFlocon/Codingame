package org.neige.codingame.keepofthegrass

import kotlin.math.max
import kotlin.math.min


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
    var isolateTile: Int = 0
    var accessibleTile: Int = 0
    var _myZone: List<Zone>? = null
    var _opponentZone: List<Zone>? = null

    val maxOperation: Int
        get() = matter / 10

    val myZone: List<Zone>
        get() {
            _myZone?.let { return it }
            return game.board.zones
                .filter { it.player == this }
                .also { _myZone = it }
        }

    val opponentZone: List<Zone>
        get() {
            _opponentZone?.let { return it }
            return game.board.zones
                .filter { it.player != this && it.player != null }
                .also { _opponentZone = it }
        }

    val behaviour: Behaviour
        get() = when {
            end != null -> Behaviour.FINISH
            myZone.any { it.borders.keys.any { it.player == opponent } } -> Behaviour.FIGHT
            else -> Behaviour.EXPLORE
        }


    override fun reset() {
        recyclerNumber = 0
        robotNumber = 0
        tileNumber = 0
        isolateTile = 0
        accessibleTile = 0
        _myZone = null
        _opponentZone = null
    }

    override fun compute() {
        game.board.fields
            .filter { it.any { it.player == this } }
            .forEach {
                val tileNumber = it.sumOf { it.tiles.filter { !it.willBecomeGrass }.size }
                if (it.all { it.player == null || it.player == this }) {
                    isolateTile += tileNumber
                }
                accessibleTile += tileNumber
            }
    }

    //TODO
    // handle cost
    // avoid conflict spawn on recycler
    // avoid conflict move on recycler
    fun actions(): List<Action> {
        return when (behaviour) {
            Behaviour.EXPLORE -> explore()
            Behaviour.FIGHT -> fight()
            Behaviour.FINISH -> finish()
        }
    }

    /**
     * Explore
     * Move in direction of enemy base
     * Build to extract resource
     * Spawn for better exploration
     */
    private fun explore(): List<Action> {
        //Region Build
        val builds = mutableListOf<Action.Build>()
        myZone.map { it.tiles }
            .flatten()
            .asSequence()
            .filter { !it.inRangeOfRecycler && it.empty }
            .sortedByDescending { it.recyclingPotential }
            .filter { tile -> builds.none { build -> game.board.tilesInRange(build.tile).any { it == tile } } }
            .map { Action.Build(it) }
            .take(min(maxOperation, max(0, 3 - recyclerNumber)))
            .forEach { builds.add(it) }
        //EndRegion Build

        //Region Move
        val opponentTile = opponentZone
            .map { it.tiles }
            .flatten()

        val moves = myZone
            .filter { it.robot > 0 }
            .flatMap { zone -> zone.tiles.filter { it.robot > 0 }.map { zone to it } }
            .flatMap { (zone, tile) ->
                opponentTile
                    .sortedBy { it.distanceTo(tile) }
                    .take(tile.robot)
                    .map {
                        Action.Move(
                            number = 1,
                            from = tile,
                            to = it
                        )
                    }
            }
        //EndRegion Move

        //Region Spawns
        //TODO spawn when more border than robot
        val spawns = game.board.fields
            .filter { it.any { it.player == game.opponent(this) } }
            .flatMap { it.filter { it.player == this } }
            .flatMap { zone -> zone.borders.flatMap { it.value }.filter { it.inside.robot == 0 } }
            .map { Action.Spawn(1, it.inside) }
            .filter { action -> !action.tile.willBecomeGrass && builds.none { it.tile == action.tile } }
            .take(max(maxOperation - builds.count(), 0))
        //EndRegion Spawns

        return (moves + spawns + builds)
    }

    //TODO
    // move to conquer
    // spawn to fight
    // build to block (behind robot < opponentRobot)
    // build don't build somewhere i will lose
    /**
     * Fight
     * Move to conquer
     * Spawn to fight
     * Build to block
     */
    private fun fight(): List<Action> {
        val opponentTile = opponentZone
            .map { it.tiles }
            .flatten()

        val moves = myZone
            .filter { it.robot > 0 }
            .flatMap { zone -> zone.tiles.filter { it.robot > 0 }.map { zone to it } }
            .flatMap { (zone, tile) ->
                opponentTile
                    .sortedBy { it.distanceTo(tile) }
                    .take(tile.robot)
                    .map {
                        Action.Move(
                            number = 1,
                            from = tile,
                            to = it
                        )
                    }
            }

        val danger = game.board.fields
            .filter { it.any { it.player == game.opponent(this) } }
            .flatMap { it.filter { it.player == this } }
            .flatMap { zone -> zone.borders.flatMap { it.value } }
            .filter { !it.inside.willBecomeGrass }


        val spawns = danger.map { Action.Spawn((matter / 10) / danger.size, it.inside) }

        val builds = myZone
            .flatMap { it.borders.flatMap { it.value }.filter { it.outside.owner != null && it.outside.owner != this && it.inside.empty } }
            .map { Action.Build(it.inside) }

        return (moves + builds + spawns)
    }

    // TODO explore & spawn when needed & no more build
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
            }
    }

    override fun debug() = """
        |$behaviour robot($robotNumber) & recycler($recyclerNumber)       
        |Accessible:$accessibleTile | Isolate:$isolateTile
    """.trimMargin()

    enum class Behaviour {
        EXPLORE,
        FIGHT,
        FINISH,
    }

}
