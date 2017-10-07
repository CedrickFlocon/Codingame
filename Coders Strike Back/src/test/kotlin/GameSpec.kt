import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*

object GameSpec : Spek({

    given("Game 1 start") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_game_1"))
        val game = Game(scanner)

        on("first step") {
            game.nextStep()

            it("should have a new position") {
                assertThat(game.position.x).isEqualTo(11661)
                assertThat(game.position.y).isEqualTo(3154)
            }
            it("should have a new opponent position") {
                assertThat(game.opponent.x).isEqualTo(10923)
                assertThat(game.opponent.y).isEqualTo(2478)
            }
            it("should have a checkpoint information") {
                assertThat(game.nextCheckpoint.x).isEqualTo(7492)
                assertThat(game.nextCheckpoint.y).isEqualTo(6966)
                assertThat(game.distance).isEqualTo(5649)
                assertThat(game.angle).isEqualTo(0)
            }
            it("should have memorised one checkpoint") {
                assertThat(game.allCheckpoint).hasSize(1)
                assertThat(game.allCheckpoint[0].x).isEqualTo(7492)
                assertThat(game.allCheckpoint[0].y).isEqualTo(6966)
            }
            it("shouldn't finish a lap") {
                assertThat(game.lap).isEqualTo(0)
            }
            it("should not be the farthest checkpoint") {
                assertThat(game.isFarthestCheckpoint()).isFalse()
            }
        }

        on("second step") {
            game.nextStep()

            it("should have a new position") {
                assertThat(game.position.x).isEqualTo(11181)
                assertThat(game.position.y).isEqualTo(3593)
            }
            it("should have a new opponent position") {
                assertThat(game.opponent.x).isEqualTo(10528)
                assertThat(game.opponent.y).isEqualTo(2994)
            }
            it("should have the same checkpoint but a different distance") {
                assertThat(game.nextCheckpoint.x).isEqualTo(7492)
                assertThat(game.nextCheckpoint.y).isEqualTo(6966)
                assertThat(game.distance).isEqualTo(4999)
                assertThat(game.angle).isEqualTo(0)
            }
        }

        on("first checkpoint reach") {
            for (i in 1..8) {
                game.nextStep()
            }

            it("should have a new position") {
                assertThat(game.position.x).isEqualTo(7681)
                assertThat(game.position.y).isEqualTo(7356)
            }
            it("should have a new opponent position") {
                assertThat(game.opponent.x).isEqualTo(7091)
                assertThat(game.opponent.y).isEqualTo(6793)
            }
            it("should have the same checkpoint but a different distance") {
                assertThat(game.nextCheckpoint.x).isEqualTo(5985)
                assertThat(game.nextCheckpoint.y).isEqualTo(5355)
                assertThat(game.distance).isEqualTo(2623)
                assertThat(game.angle).isEqualTo(50)
            }
            it("should have memorised two checkpoint") {
                assertThat(game.allCheckpoint).hasSize(2)
                assertThat(game.allCheckpoint[0].x).isEqualTo(7492)
                assertThat(game.allCheckpoint[0].y).isEqualTo(6966)
                assertThat(game.allCheckpoint[1].x).isEqualTo(5985)
                assertThat(game.allCheckpoint[1].y).isEqualTo(5355)
            }
            it("shouldn't finish a lap") {
                assertThat(game.lap).isEqualTo(0)
            }
            it("should not be the farthest checkpoint") {
                assertThat(game.isFarthestCheckpoint()).isFalse()
            }

        }

        on("first lap") {
            for (i in 1..28) {
                game.nextStep()
            }

            it("should memorise three checkpoint") {
                assertThat(game.allCheckpoint).hasSize(3)
                assertThat(game.allCheckpoint[0].x).isEqualTo(7492)
                assertThat(game.allCheckpoint[0].y).isEqualTo(6966)
                assertThat(game.allCheckpoint[1].x).isEqualTo(5985)
                assertThat(game.allCheckpoint[1].y).isEqualTo(5355)
                assertThat(game.allCheckpoint[2].x).isEqualTo(11292)
                assertThat(game.allCheckpoint[2].y).isEqualTo(2816)
            }
            it("should finish one lap") {
                assertThat(game.lap).isEqualTo(1)
            }
            it("should not be the farthest checkpoint") {
                assertThat(game.isFarthestCheckpoint()).isFalse()
            }
        }

        on("farthest checkpoint") {
            for (i in 1..35){
                game.nextStep()
            }

            it("should be the farthest checkpoint") {
                assertThat(game.isFarthestCheckpoint()).isTrue()
            }
        }

        on("second lap") {
            for (i in 1..13) {
                game.nextStep()
            }
            it("should finish the second lap") {
                assertThat(game.lap).isEqualTo(2)
            }
        }

        on("last step") {
            for (i in 1..59) {
                game.nextStep()
            }

            it("should have a new position") {
                assertThat(game.position.x).isEqualTo(11197)
                assertThat(game.position.y).isEqualTo(3557)
            }
            it("should have a new opponent position") {
                assertThat(game.opponent.x).isEqualTo(9494)
                assertThat(game.opponent.y).isEqualTo(1881)
            }
            it("should have a checkpoint information") {
                assertThat(game.nextCheckpoint.x).isEqualTo(11292)
                assertThat(game.nextCheckpoint.y).isEqualTo(2816)
                assertThat(game.distance).isEqualTo(747)
                assertThat(game.angle).isEqualTo(-20)
            }
            it("should memorise three checkpoint") {
                assertThat(game.allCheckpoint).hasSize(3)
                assertThat(game.allCheckpoint[0].x).isEqualTo(7492)
                assertThat(game.allCheckpoint[0].y).isEqualTo(6966)
                assertThat(game.allCheckpoint[1].x).isEqualTo(5985)
                assertThat(game.allCheckpoint[1].y).isEqualTo(5355)
                assertThat(game.allCheckpoint[2].x).isEqualTo(11292)
                assertThat(game.allCheckpoint[2].y).isEqualTo(2816)
            }
            it("should still be the second lap") {
                assertThat(game.lap).isEqualTo(2)
            }
            it("should not be the farthest checkpoint") {
                assertThat(game.isFarthestCheckpoint()).isFalse()
            }
        }
    }

})