package org.neige.codingame.soak

import org.neige.codingame.geometry.Coordinate
import java.util.*

val game = Game()

fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    val myId = input.nextInt() // Your player id (0 or 1)
    val agentDataCount = input.nextInt() // Total number of agents in the game

    for (i in 0 until agentDataCount) {
        val agentId = input.nextInt() // Unique identifier for this agent
        val player = input.nextInt() // Player id of this agent
        val shootCooldown = input.nextInt() // Number of turns between each of this agent's shots
        val optimalRange = input.nextInt() // Maximum manhattan distance for greatest damage output
        val soakingPower = input.nextInt() // Damage output within optimal conditions
        val splashBombs = input.nextInt() // Number of splash bombs this can throw this game

        game.agents[agentId] = Agent(
            id = agentId, player = if (player == myId) Player.ME else Player.OPPONENT, optimalRange = optimalRange, soakingPower = soakingPower
        )
    }

    val width = input.nextInt() // Width of the game map
    val height = input.nextInt() // Height of the game map
    val grid = mutableMapOf<Coordinate, TileType>()

    for (i in 0 until height) {
        for (j in 0 until width) {
            val x = input.nextInt() // X coordinate, 0 is left edge
            val y = input.nextInt() // Y coordinate, 0 is top edge

            val tileType = input.nextInt()

            grid[Coordinate(x, y)] = TileType.fromInt(tileType)
        }
    }
    game.compute(Size(width, height), grid)

    // game loop
    while (true) {
        val agentCount = input.nextInt() // Total number of agents still in the game

        game.agents.values.forEach { it.wetness = 100 }
        for (i in 0 until agentCount) {
            val agentId = input.nextInt()
            val x = input.nextInt()
            val y = input.nextInt()
            val cooldown = input.nextInt() // Number of turns before this agent can shoot
            val splashBombs = input.nextInt()
            val wetness = input.nextInt() // Damage (0-100) this agent has taken

            game.agents[agentId]!!.let {
                it.coordinate = Coordinate(x, y)
                it.wetness = wetness
            }
        }
        game.agents.values.removeAll { it.wetness >= 100 }

        val myAgentCount = input.nextInt() // Number of alive agents controlled by you

        game.play()
    }
}
