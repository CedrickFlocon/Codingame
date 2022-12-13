package org.neige.codingame.keepofthegrass

class Player(
    private val board: Board
) {

    var matter: Int = 0

    fun play() {
        val actions = mutableListOf<String>()
        actions.addAll(board.tiles
            .filter { it.owner == Owner.ME && it.units > 0 }
            .map { tile ->
                val inRange = board.tilesInRange(tile)
                    .filter { it.owner != Owner.ME && !it.nextTurnGrass && it.free }

                Action.move(
                    number = tile.units,
                    from = tile,
                    to = if (inRange.isNotEmpty()) inRange.random() else board.tiles.filter { it.owner != Owner.ME }.random()
                )
            })

        if (board.tiles.count { it.recycler && it.owner == Owner.ME } < 3) {
            actions.addAll(
                board.tiles
                    .asSequence()
                    .filter { it.canBuild }
                    .filter { it.recyclingPotential > 15 }
                    .sortedByDescending { it.recyclingPotential }
                    .filter { !it.inRangeOfRecycler }
                    .take(1)
                    .map { Action.build(it) }
                    .toList()
            )
        }

        actions.addAll(board.tiles
            .filter { it.canSpawn && !it.nextTurnGrass }
            .filter { tile -> board.zones.first { it.tiles.any { it == tile } }.let { it.neutralTile > 0 || it.opponentTile > 0 } }
            .map { Action.spawn(1, it) }
        )

        actions.add(Action.wait())
        println(actions.joinToString(";"))
        actions.clear()
    }

}
