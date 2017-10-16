package org.neige.codingame.codersstrikeback.pod

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.neige.codingame.codersstrikeback.*


object HunterSpec : Spek({

    given("a hunter") {
        val game = Mockito.mock(Game::class.java)
        val hunter = Hunter(game, Coordinate(0.0, 0.0), Scalar(10.0, 10.0), 10, Checkpoint(Coordinate(10.0, 10.0)))

        given("two opponent") {
            val opponent1 = mock(Pod::class.java)
            val opponent2 = mock(Pod::class.java)
            beforeGroup {
                given(game.opponents).willReturn(listOf(opponent1, opponent2))
            }

            describe("collide") {

                given("hunter will collide opponent1") {
                    beforeEachTest {
                        given(opponent1.coordinate).willReturn(Coordinate(1000.0, 0.0))
                        given(opponent1.speed).willReturn(Scalar(-1000.0, 1000.0))
                        given(opponent2.coordinate).willReturn(Coordinate(0.0, 0.0))
                        given(opponent2.speed).willReturn(Scalar(0.0, 0.0))
                        hunter.coordinate = Coordinate(0.0, 0.0)
                        hunter.speed = Scalar(1000.0, 1000.0)
                    }

                    on("move") {
                        val move = hunter.move()
                        it("should shield") {
                            assertThat(move).isEqualTo(Move(hunter.getBestPath(hunter.nextCheckpoint), "SHIELD"))
                        }
                    }
                }

                given("a game input") {
                    beforeGroup {
                        given(opponent1.coordinate).willReturn(Coordinate(7162.0, 5059.0))
                        given(opponent1.speed).willReturn(Scalar(28.0, 448.0))
                        given(opponent2.coordinate).willReturn(Coordinate(0.0, 0.0))
                        given(opponent2.speed).willReturn(Scalar(0.0, 0.0))
                        hunter.coordinate = Coordinate(7466.0, 5957.0)
                        hunter.speed = Scalar(-414.0, -82.0)
                    }

                    on("move") {
                        val move = hunter.move()
                        it("should shield") {
                            assertThat(move).isEqualTo(Move(hunter.getBestPath(hunter.nextCheckpoint), "SHIELD"))
                        }
                    }
                }

            }

            given("opponent1 in first position") {
                beforeGroup {
                    given(opponent1.compareTo(opponent2)).willReturn(1)
                }

                given("hunter following the opponent1") {
                    val opponentNextCheckpoint = Checkpoint(Coordinate(-3000.0, 0.0))

                    beforeGroup {
                        given(opponent1.coordinate).willReturn(Coordinate(1000.0, 0.0))
                        given(opponent1.speed).willReturn(Scalar(10.0, 10.0))
                        given(opponent1.nextCheckpoint).willReturn(Checkpoint(Coordinate(2000.0, 0.0)))
                        given(opponent2.coordinate).willReturn(Coordinate(0.0, 0.0))
                        given(opponent2.speed).willReturn(Scalar(0.0, 0.0))
                        given(game.nextCheckpoint(opponent1.nextCheckpoint)).willReturn(opponentNextCheckpoint)
                        hunter.coordinate = Coordinate(500.0, 0.0)
                        hunter.speed = Scalar(10.0, 10.0)
                    }

                    on("move") {
                        val move = hunter.move()
                        it("should go to next checkpoint of the opponent1") {
                            assertThat(move).isEqualTo(Move(opponentNextCheckpoint.coordinate, "100"))
                        }
                    }
                }

                given("hunter close to the next opponent1 checkpoint") {
                    val opponentNextCheckpoint = Checkpoint(Coordinate(-1000.0, 0.0))

                    beforeGroup {
                        given(opponent1.coordinate).willReturn(Coordinate(1000.0, 0.0))
                        given(opponent1.speed).willReturn(Scalar(10.0, 10.0))
                        given(opponent1.nextCheckpoint).willReturn(Checkpoint(Coordinate(2000.0, 0.0)))
                        given(opponent2.coordinate).willReturn(Coordinate(0.0, 0.0))
                        given(opponent2.speed).willReturn(Scalar(0.0, 0.0))
                        given(game.nextCheckpoint(opponent1.nextCheckpoint)).willReturn(opponentNextCheckpoint)
                        hunter.coordinate = Coordinate(-1000.0, 0.0)
                        hunter.speed = Scalar(10.0, 10.0)
                    }

                    on("move") {
                        val move = hunter.move()
                        it("should wait the opponent1") {
                            assertThat(move).isEqualTo(Move(opponent1.coordinate, "0"))
                        }
                    }
                }

                given("hunter ahead of the opponent1") {
                    beforeGroup {
                        given(opponent1.coordinate).willReturn(Coordinate(1000.0, 0.0))
                        given(opponent1.speed).willReturn(Scalar(10.0, 10.0))
                        given(opponent1.nextCheckpoint).willReturn(Checkpoint(Coordinate(-1000.0, 0.0)))
                        given(opponent2.coordinate).willReturn(Coordinate(0.0, 0.0))
                        given(opponent2.speed).willReturn(Scalar(0.0, 0.0))
                        hunter.coordinate = Coordinate(-1000.0, 0.0)
                        hunter.speed = Scalar(10.0, 10.0)
                    }

                    on("move") {
                        val move = hunter.move()
                        it("charge the opponent1") {
                            assertThat(move).isEqualTo(Move(opponent1.coordinate, "100"))
                        }
                    }
                }
            }

        }
    }
})