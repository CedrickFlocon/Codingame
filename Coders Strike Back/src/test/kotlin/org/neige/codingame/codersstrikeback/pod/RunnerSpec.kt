package org.neige.codingame.codersstrikeback.pod

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mockito
import org.neige.codingame.codersstrikeback.*

object RunnerSpec : Spek({

    describe("a runner") {
        val game = Mockito.mock(Game::class.java)
        val runner = Runner(game, Coordinate(0.0, 0.0), Scalar(10.0, 10.0), 10, Checkpoint(Coordinate(1000.0, 1000.0)))
        val opponent1 = Mockito.mock(Pod::class.java)
        val opponent2 = Mockito.mock(Pod::class.java)
        val hunter = Mockito.mock(Ally::class.java)

        beforeEachTest {
            given(game.opponents).willReturn(listOf(opponent1, opponent2))
            given(game.pods).willReturn(listOf(hunter, runner))

            given(opponent1.coordinate).willReturn(Coordinate(0.0, 0.0))
            given(opponent1.speed).willReturn(Scalar(0.0, 0.0))
            given(opponent2.coordinate).willReturn(Coordinate(0.0, 0.0))
            given(opponent2.speed).willReturn(Scalar(0.0, 0.0))
            given(hunter.coordinate).willReturn(Coordinate(0.0, 0.0))
            given(hunter.speed).willReturn(Scalar(0.0, 0.0))
            runner.coordinate = Coordinate(0.0, 0.0)
            runner.speed = Scalar(0.0, 0.0)

        }

        describe("this runner close to the next checkpoint") {
            beforeEachTest {
                given(game.nextCheckpoint(any() ?: Checkpoint(Coordinate(0.0, 0.0))))
                        .willReturn(Checkpoint(Coordinate(2000.0, 2000.0)))
                runner.nextCheckpoint = Checkpoint(Coordinate(1000.0, 1000.0))
            }

            on("move") {
                val move = runner.move()

                it("should move without a boost") {
                    assertThat(move).isEqualTo(Move(runner.nextCheckpoint.coordinate - runner.speed, "200"))
                }
            }
        }

        describe("this runner distant from next checkpoint") {

            given("not the farthest checkpoint") {
                beforeEachTest {
                    given(game.isFarthestCheckpoint(runner.nextCheckpoint)).willReturn(false)
                }

                on("move") {
                    val move = runner.move()

                    it("should move without a boost") {
                        assertThat(move).isEqualTo(Move(runner.getBestPath(runner.nextCheckpoint), "200"))
                    }
                }
            }

            given("a farthest checkpoint") {
                beforeEachTest {
                    runner.nextCheckpoint = Checkpoint(5000.0, 5000.0)
                    runner.coordinate = Coordinate(0.0, 0.0)
                    given(game.isFarthestCheckpoint(runner.nextCheckpoint)).willReturn(true)
                }

                given("a bad angle") {
                    beforeEachTest {
                        runner.speed = Scalar(10.0, 0.0)
                    }

                    on("move") {
                        val move = runner.move()

                        it("should move without a boost") {
                            assertThat(move).isEqualTo(Move(runner.getBestPath(runner.nextCheckpoint), "200"))
                        }
                    }
                }

                given("a good angle") {
                    beforeEachTest {
                        runner.speed = Scalar(10.0, 10.0)
                    }

                    on("move") {
                        val move = runner.move()

                        it("should move with a boost") {
                            assertThat(move).isEqualTo(Move(runner.getBestPath(runner.nextCheckpoint), "BOOST"))
                        }
                    }

                    on("move again") {
                        val move = runner.move()

                        it("should move without a boost") {
                            assertThat(move).isEqualTo(Move(runner.getBestPath(runner.nextCheckpoint), "200"))
                        }
                    }
                }
            }

        }

        describe("this runner in 2 step in the checkpoint") {
            val nextCheckpoint = Checkpoint(Coordinate(-1000.0, 1000.0))
            beforeEachTest {
                runner.speed = Scalar(100.0, 0.0)
                runner.coordinate = Coordinate(250.0, 0.0)
                runner.nextCheckpoint = Checkpoint(Coordinate(1000.0, 0.0))
                given(game.nextCheckpoint(any() ?: Checkpoint(Coordinate(0.0, 0.0))))
                        .willReturn(nextCheckpoint)
            }

            on("move") {
                val move = runner.move()

                it("should move toward to the next checkpoint") {
                    assertThat(move).isEqualTo(Move(runner.getBestPath(nextCheckpoint), "0"))
                }
            }

        }

        describe("a runner will collide opponent1") {
            beforeEachTest {
                given(opponent1.coordinate).willReturn(Coordinate(2000.0, 2000.0))
                given(opponent1.speed).willReturn(Scalar(-1000.0, -1000.0))
                runner.coordinate = Coordinate(1000.0, 1000.0)
                runner.speed = Scalar(1000.0, 1000.0)
            }

            on("move") {
                val move = runner.move()
                it("should shield") {
                    assertThat(move).isEqualTo(Move(runner.getBestPath(runner.nextCheckpoint), "SHIELD"))
                }
            }
        }

        describe("a runner will collide opponent2") {
            beforeEachTest {
                given(opponent2.coordinate).willReturn(Coordinate(11738.0, 5684.0))
                given(opponent2.speed).willReturn(Scalar(1.0, 163.0))
                runner.coordinate = Coordinate(10956.0, 6590.0)
                runner.speed = Scalar(439.0, -202.0)
            }

            on("move") {
                val move = runner.move()
                it("should shield") {
                    assertThat(move).isEqualTo(Move(runner.getBestPath(runner.nextCheckpoint), "SHIELD"))
                }
            }
        }

        describe("a runner will collide hunter") {
            beforeEachTest {
                given(hunter.coordinate).willReturn(Coordinate(11738.0, 5684.0))
                given(hunter.speed).willReturn(Scalar(1.0, 163.0))
                runner.coordinate = Coordinate(10956.0, 6590.0)
                runner.speed = Scalar(439.0, -202.0)
            }

            on("move") {
                val move = runner.move()
                it("should shield") {
                    assertThat(move).isEqualTo(Move(runner.getBestPath(runner.nextCheckpoint), "SHIELD"))
                }
            }
        }

        describe("a game bug runner collide opponent2") {
            beforeEachTest {
                given(opponent2.coordinate).willReturn(Coordinate(8312.0, 2959.0))
                given(opponent2.speed).willReturn(Scalar(-359.0, -111.0))
                runner.coordinate = Coordinate(7023.0, 2429.0)
                runner.speed = Scalar(344.0, 19.0)
            }

            on("move") {
                val move = runner.move()
                it("should shield") {
                    assertThat(move).isEqualTo(Move(runner.getBestPath(runner.nextCheckpoint), "SHIELD"))
                }
            }
        }

        describe("a runner will not collide opponent") {
            beforeGroup {
                runner.coordinate = Coordinate(1000.0, 1000.0)
                runner.speed = Scalar(100.0, 100.0)
            }

            on("move") {
                val move = runner.move()
                it("should keep moving") {
                    assertThat(move).isEqualTo(Move(runner.getBestPath(runner.nextCheckpoint), "200"))
                }
            }
        }

        describe("a speed scalar") {
            beforeEachTest {
                runner.speed = Scalar(4.0, 4.0)
            }
            on("best path") {

                val bestPath = runner.getBestPath(runner.nextCheckpoint)

                it("should care of the speed scalar") {
                    assertThat(bestPath).isEqualTo(runner.nextCheckpoint.coordinate - runner.speed * 3.0)
                }
            }
        }

    }

})