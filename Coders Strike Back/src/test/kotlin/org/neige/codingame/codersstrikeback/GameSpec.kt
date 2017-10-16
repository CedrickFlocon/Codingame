package org.neige.codingame.codersstrikeback

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.neige.codingame.codersstrikeback.pod.Ally
import org.neige.codingame.codersstrikeback.pod.Pod
import java.util.*

object GameSpec : Spek({

    given("Game 1 start") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_one_step.txt"))
        val pod1 = mock(Ally::class.java)
        val pod2 = mock(Ally::class.java)

        val opponent1 = mock(Pod::class.java)
        val opponent2 = mock(Pod::class.java)

        val checkpoints = listOf(Checkpoint(Coordinate(14657.0, 1416.0)),
                Checkpoint(Coordinate(5284.0, 4297.0)),
                Checkpoint(Coordinate(8348.0, 1789.0)))

        val game = Game(scanner, 3, checkpoints)
        game.init(listOf(pod1, pod2), listOf(opponent1, opponent2))

        on("next step") {
            game.nextStep()

            it("should update pod1") {
                verify(pod1).coordinate = Coordinate(5937.0, 3740.0)
                verify(pod1).speed = Scalar(82.0, -21.0)
                verify(pod1).angle = 345
                verify(pod1).nextCheckpoint = Checkpoint(Coordinate(14657.0, 1416.0))
            }

            it("should update pod2") {
                verify(pod2).coordinate = Coordinate(6243.0, 4680.0)
                verify(pod2).speed = Scalar(79.0, -30.0)
                verify(pod2).angle = 339
                verify(pod2).nextCheckpoint = Checkpoint(Coordinate(14657.0, 1416.0))
            }

            it("should update opponent1") {
                verify(opponent1).coordinate = Coordinate(5629.0, 2800.0)
                verify(opponent1).speed = Scalar(84.0, -12.0)
                verify(opponent1).angle = 351
                verify(opponent1).nextCheckpoint = Checkpoint(Coordinate(14657.0, 1416.0))
            }

            it("should update opponent2") {
                verify(opponent2).coordinate = Coordinate(6549.0, 5621.0)
                verify(opponent2).speed = Scalar(75.0, -39.0)
                verify(opponent2).angle = 333
                verify(opponent2).nextCheckpoint = Checkpoint(Coordinate(14657.0, 1416.0))
            }
        }

        on("play") {
            val move2 = mock(Move::class.java)
            val move1 = mock(Move::class.java)
            given(pod1.move()).willReturn(move1)
            given(pod2.move()).willReturn(move2)

            val play = game.play()

            it("should call pod1 move") {
                assertThat(play[0]).isEqualTo(move1)
            }

            it("should call pod2 move") {
                assertThat(play[1]).isEqualTo(move2)
            }

        }

        describe("next checkpoint") {

            on("find next checkpoint of the 1st") {
                val nextCheckpoint = game.nextCheckpoint(checkpoints[0])

                it("should return the 2nd checkpoint") {
                    assertThat(nextCheckpoint).isEqualTo(checkpoints[1])
                }
            }

            on("find next checkpoint of the 3rd") {
                val nextCheckpoint = game.nextCheckpoint(checkpoints[2])

                it("should return the 1st checkpoint") {
                    assertThat(nextCheckpoint).isEqualTo(checkpoints[0])
                }
            }
        }

        describe("previous checkpoint") {

            on("find previous checkpoint of the 1st") {
                val nextCheckpoint = game.previousCheckpoint(checkpoints[0])

                it("should return the 3rd checkpoint") {
                    assertThat(nextCheckpoint).isEqualTo(checkpoints[2])
                }
            }

            on("find previous checkpoint of the 3rd") {
                val nextCheckpoint = game.previousCheckpoint(checkpoints[2])

                it("should return the 2nd checkpoint") {
                    assertThat(nextCheckpoint).isEqualTo(checkpoints[1])
                }
            }
        }

        describe("is farthest") {
            on("is first checkpoint is farthest one") {
                val farthestCheckpoint = game.isFarthestCheckpoint(checkpoints[0])
                it("should be false") {
                    assertThat(farthestCheckpoint).isFalse()
                }
            }

            on("is second checkpoint is farthest one") {
                val farthestCheckpoint = game.isFarthestCheckpoint(checkpoints[1])
                it("should be true") {
                    assertThat(farthestCheckpoint).isTrue()
                }
            }

            on("is third checkpoint is farthest one") {
                val farthestCheckpoint = game.isFarthestCheckpoint(checkpoints[2])
                it("should be false") {
                    assertThat(farthestCheckpoint).isFalse()
                }
            }

        }

    }

})