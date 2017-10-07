import java.util.*

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
fun main(args: Array<String>) {
    var move: Move
    val game = Game(Scanner(System.`in`))

    // game loop
    while (true) {
        game.nextStep()

        move = Move(game.nextCheckpoint)

        if (game.angle == 0 && game.isBoostAvailable && game.isFarthestCheckpoint()) {
            move.thrust = "BOOST"
            game.isBoostAvailable = false
        } else if (game.distance < 2000) {
            when (game.angle) {
                in -27..27 -> move.thrust = "100"
                in -45..45 -> move.thrust = "75"
                else -> move.thrust = "20"
            }
        } else {
            when (game.angle) {
                in -45..45 -> move.thrust = "100"
                in -90..90 -> move.thrust = "75"
                else -> move.thrust = "20"
            }
        }

        println(move.move())
    }

}

class Game(val input: Scanner) {

    var position = Coordinate(0, 0)
        private set
    var opponent = Coordinate(0, 0)
        private set
    var nextCheckpoint = Coordinate(0, 0)
        private set
    var currentCheckpoint: Coordinate? = null
        private set
    var distance = 0
        private set
    var angle = 0
        private set
    var allCheckpoint = emptyList<Coordinate>()
        private set
    var lap = 0
        private set
    var isBoostAvailable = true

    fun nextStep() {
        val position = Coordinate(input.nextInt(), input.nextInt())
        val nextCheckpoint = Coordinate(input.nextInt(), input.nextInt())
        val distance = input.nextInt()
        val angle = input.nextInt()
        val opponent = Coordinate(input.nextInt(), input.nextInt())

        if (this.nextCheckpoint != nextCheckpoint && allCheckpoint.isNotEmpty()) {
            currentCheckpoint = this.nextCheckpoint
            if (allCheckpoint[0] == nextCheckpoint) {
                lap++
            }
        }

        if (!allCheckpoint.contains(nextCheckpoint)) {
            allCheckpoint += nextCheckpoint
        }

        this.position = position
        this.nextCheckpoint = nextCheckpoint
        this.distance = distance
        this.angle = angle
        this.opponent = opponent

        System.err.println("$this")
    }

    fun isFarthestCheckpoint(): Boolean {
        if (lap < 1) return false
        val currentCheckpoint = this.currentCheckpoint ?: return false
        val distanceBetweenCurrentCheckpoint = currentCheckpoint.distanceFrom(getNextCheckpoint(currentCheckpoint))

        return allCheckpoint.none { it.distanceFrom(getNextCheckpoint(it)) > distanceBetweenCurrentCheckpoint }
    }

    private fun getNextCheckpoint(coordinate: Coordinate): Coordinate {
        return if (allCheckpoint.indexOf(coordinate) == allCheckpoint.size - 1) allCheckpoint[0] else allCheckpoint[allCheckpoint.indexOf(coordinate) + 1]
    }

    override fun toString(): String {
        return "position : ${position.x}:${position.y} \n" +
                "opponent : ${opponent.x}:${opponent.y}" +
                "Current checkpoint : ${currentCheckpoint?.x}:${currentCheckpoint?.y}\n" +
                "Next checkpoint : ${nextCheckpoint.x}:${nextCheckpoint.y} ${distance} ${angle}°\n" +
                "Lap $lap"
    }
}

data class Coordinate(val x: Int, val y: Int) {
    fun distanceFrom(coordinate: Coordinate): Double {
        return Math.sqrt(Math.pow((coordinate.y - y).toDouble(), 2.0) + Math.pow((coordinate.x - x).toDouble(), 2.0));
    }
}

data class Move(private val coordinate: Coordinate, var thrust: String? = null) {

    fun move(): String {
        if (thrust == null) {
            throw IllegalStateException("You must init thrust before moving")
        }
        return "${coordinate.x} ${coordinate.y} $thrust"
    }

}
