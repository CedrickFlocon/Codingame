package org.neige.codingame.codersstrikeback

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


object PodSpec : Spek({

    given("a pod") {
        val pod = Pod()
        pod.position = Coordinate(0.0, 0.0)
        pod.position = Coordinate(1.0, 1.0)

        given("an opponent far from the pod") {
            val opponentPod = Pod()
            opponentPod.position = Coordinate(pod.position.x + 1000, pod.position?.y + 1000)

            on("move pod to one checkpoint") {
                val move = pod.move(Coordinate(5.0, 5.0), false, 0, opponentPod)
                it("should move to") {
                    assertThat(move.thrust).isEqualTo("100")
                }
            }

            on("move pod to the farthest checkpoint") {
                val move = pod.move(Coordinate(5.0, 5.0), true, 0, opponentPod)
                it("should move with a boost") {
                    assertThat(move.thrust).isEqualTo("BOOST")
                }
            }

            on("move pod to the farthest checkpoint again") {
                val move = pod.move(Coordinate(5.0, 5.0), true, 0, opponentPod)
                it("should move with a boost") {
                    assertThat(move.thrust).isEqualTo("100")
                }
            }
        }
    }

    given("breakitdown pod") {
        val myPod = Pod()
        val opponentPod = Pod()

        on("init position") {
            myPod.position = Coordinate(11790.0, 4204.0)
            opponentPod.position = Coordinate(11340.0, 5347.0)

            it("should not collided") {
                assertThat(myPod.willCollide(opponentPod)).isFalse()
            }
        }

        on("on follow opponent") {
            myPod.position = Coordinate(11148.0, 4488.0)
            opponentPod.position = Coordinate(10761.0, 5527.0)

            myPod.position = Coordinate(10509.0, 4763.0)
            opponentPod.position = Coordinate(10172.0, 5703.0)

            it("should not collide") {
                assertThat(myPod.willCollide(opponentPod)).isFalse()
            }
        }

        on("on move toward opponent") {
            myPod.position = Coordinate(9872.0, 5029.0)
            opponentPod.position = Coordinate(9575.0, 5874.0)

            it("should collide") {
                assertThat(myPod.willCollide(opponentPod)).isTrue()
            }
            it("should not trigger shield") {
                val move = myPod.move(Coordinate(0.0, 0.0), false, 0, opponentPod)
                assertThat(move.thrust).isNotEqualTo("SHIELD")
            }

        }
    }

})