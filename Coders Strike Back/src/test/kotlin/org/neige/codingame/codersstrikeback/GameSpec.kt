package org.neige.codingame.codersstrikeback

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.BDDMockito
import org.mockito.Mockito.*
import java.util.*

object GameSpec : Spek({

    given("Game 1 start") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_race_1"))
        val myPod = mock(Pod::class.java)
        val opponentPod = mock(Pod::class.java)
        val move = Move(Coordinate(0.0, 0.0))
        move.thrust = "100"
        BDDMockito.given(myPod.move(any(Coordinate::class.java) ?: Coordinate(0.0, 0.0), anyBoolean(), anyInt(), any() ?: Pod()))
                .willReturn(move)
        val game = Game(myPod, opponentPod, scanner)

        on("first step") {
            game.nextStep()

            it("should have a new position") {
                verify(myPod).position = Coordinate(11661.0, 3154.0)
            }
            it("should have a new opponent position") {
                verify(opponentPod).position = Coordinate(10923.0, 2478.0)
            }
            it("should have a checkpoint information") {
                assertThat(game.nextCheckpoint.x).isEqualTo(7492.0)
                assertThat(game.nextCheckpoint.y).isEqualTo(6966.0)
            }
            it("should have memorised one checkpoint") {
                assertThat(game.allCheckpoint).hasSize(1)
                assertThat(game.allCheckpoint[0].x).isEqualTo(7492.0)
                assertThat(game.allCheckpoint[0].y).isEqualTo(6966.0)
            }
            it("should move the pod") {
                verify(myPod).move(game.nextCheckpoint, false, 0, opponentPod)
            }

            it("shouldn't finish a lap") {
                assertThat(game.lap).isEqualTo(1)
            }
        }

        on("second step") {
            game.nextStep()

            it("should have a new position") {
                verify(myPod).position = Coordinate(11181.0, 3593.0)
            }
            it("should have a new opponent position") {
                verify(opponentPod).position = Coordinate(10528.0, 2994.0)
            }
            it("should have the same checkpoint but a different distance") {
                assertThat(game.nextCheckpoint.x).isEqualTo(7492.0)
                assertThat(game.nextCheckpoint.y).isEqualTo(6966.0)
                verify(myPod, times(2)).move(game.nextCheckpoint, false, 0, opponentPod)
            }
        }

        on("first checkpoint reach") {
            for (i in 1..8) {
                game.nextStep()
            }

            it("should have a new position") {
                verify(myPod).position = Coordinate(7681.0, 7356.0)
            }
            it("should have a new opponent position") {
                verify(opponentPod).position = Coordinate(7091.0, 6793.0)
            }
            it("should have the same checkpoint but a different distance") {
                assertThat(game.nextCheckpoint.x).isEqualTo(5985.0)
                assertThat(game.nextCheckpoint.y).isEqualTo(5355.0)
            }
            it("should move the pod") {
                verify(myPod).move(game.nextCheckpoint, false, 50, opponentPod)
            }
            it("should have memorised two checkpoint") {
                assertThat(game.allCheckpoint).hasSize(2)
                assertThat(game.allCheckpoint[0].x).isEqualTo(7492.0)
                assertThat(game.allCheckpoint[0].y).isEqualTo(6966.0)
                assertThat(game.allCheckpoint[1].x).isEqualTo(5985.0)
                assertThat(game.allCheckpoint[1].y).isEqualTo(5355.0)
            }
            it("shouldn't finish a lap") {
                assertThat(game.lap).isEqualTo(1)
            }
            it("should move the pod") {
                verify(myPod).move(Coordinate(5985.0, 5355.0), false, 50, opponentPod)
            }

        }

        on("first lap") {
            for (i in 1..28) {
                game.nextStep()
            }

            it("should memorise three checkpoint") {
                assertThat(game.allCheckpoint).hasSize(3)
                assertThat(game.allCheckpoint[0].x).isEqualTo(7492.0)
                assertThat(game.allCheckpoint[0].y).isEqualTo(6966.0)
                assertThat(game.allCheckpoint[1].x).isEqualTo(5985.0)
                assertThat(game.allCheckpoint[1].y).isEqualTo(5355.0)
                assertThat(game.allCheckpoint[2].x).isEqualTo(11292.0)
                assertThat(game.allCheckpoint[2].y).isEqualTo(2816.0)
            }
            it("should finish one lap") {
                assertThat(game.lap).isEqualTo(2)
            }
            it("should move the pod") {
                verify(myPod).move(Coordinate(7492.0, 6966.0), false, 81, opponentPod)
            }
        }

        on("farthest checkpoint") {
            for (i in 1..34) {
                game.nextStep()
            }

            it("should move the pod") {
                verify(myPod).move(Coordinate(11292.0, 2816.0), true, -87, opponentPod)
            }
        }

        on("second lap") {
            for (i in 1..14) {
                game.nextStep()
            }
            it("should finish the second lap") {
                assertThat(game.lap).isEqualTo(3)
            }
        }

        on("last step") {
            for (i in 1..59) {
                game.nextStep()
            }

            it("should have a new position") {
                verify(myPod).position = Coordinate(11197.0, 3557.0)
            }
            it("should have a new opponent position") {
                verify(opponentPod).position = Coordinate(9494.0, 1881.0)
            }
            it("should have a checkpoint information") {
                assertThat(game.nextCheckpoint.x).isEqualTo(11292.0)
                assertThat(game.nextCheckpoint.y).isEqualTo(2816.0)
            }
            it("should move the pod") {
                verify(myPod).move(game.nextCheckpoint, true, -20, opponentPod)
            }

            it("should memorise three checkpoint") {
                assertThat(game.allCheckpoint).hasSize(3)
                assertThat(game.allCheckpoint[0].x).isEqualTo(7492.0)
                assertThat(game.allCheckpoint[0].y).isEqualTo(6966.0)
                assertThat(game.allCheckpoint[1].x).isEqualTo(5985.0)
                assertThat(game.allCheckpoint[1].y).isEqualTo(5355.0)
                assertThat(game.allCheckpoint[2].x).isEqualTo(11292.0)
                assertThat(game.allCheckpoint[2].y).isEqualTo(2816.0)
            }
            it("should still be the second lap") {
                assertThat(game.lap).isEqualTo(3)
            }
        }
    }

})