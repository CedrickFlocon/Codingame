import java.util.*

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args: Array<String>) {
    val input = Scanner(System.`in`)
    var move: Move
    var game = Game(
            Coordinate(0, 0),
            Checkpoint(Coordinate(0, 0), 0, 0),
            Coordinate(0, 0),
            true)

    // game loop
    while (true) {
        game = game.copy(
                position = Coordinate(input.nextInt(), input.nextInt()),
                nextCheckPoint = Checkpoint(Coordinate(input.nextInt(), input.nextInt()), input.nextInt(), input.nextInt()),
                opponent = Coordinate(input.nextInt(), input.nextInt()))

        System.err.println("$game")

        move = Move(game.nextCheckPoint.coordinate)

        if (game.nextCheckPoint.angle == 0 && game.nextCheckPoint.distance > 5000 && game.boostAvailable) {
            move.thrust = "BOOST"
            game.boostAvailable = false
        } else {
            when (game.nextCheckPoint.angle) {
                in -90..90 -> move.thrust = "100"
                else -> move.thrust = "5"
            }
        }

        println(move.move())
    }

}

data class Game(val position: Coordinate, val nextCheckPoint: Checkpoint, val opponent: Coordinate, var boostAvailable: Boolean) {
    override fun toString(): String {
        return "position : ${position.x}:${position.y} \n" +
                "checkpoint : ${nextCheckPoint.coordinate.x}:${nextCheckPoint.coordinate.y} ${nextCheckPoint.distance} ${nextCheckPoint.angle}°"
    }
}

data class Checkpoint(val coordinate: Coordinate, val distance: Int, val angle: Int)

data class Coordinate(val x: Int, val y: Int)

data class Move(private val coordinate: Coordinate, var thrust: String? = null) {

    fun move(): String {
        if (thrust == null){
            throw IllegalStateException("You must init thrust before moving")
        }
        return "${coordinate.x} ${coordinate.y} $thrust"
    }

}
