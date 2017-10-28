package org.neige.codingame.codersstrikeback

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on


object CheckpointSpec : Spek({

    given("a checkpoint") {
        val checkpoint = Checkpoint(Coordinate(0.0, 0.0))

        given("a coordinate in the checkpoint") {
            val coordinate = Coordinate(500.0, 0.0)

            on("isInCheckpoint") {
                val inCheckpoint = checkpoint.isInCheckpoint(coordinate)

                it("should be true") {
                    assertThat(inCheckpoint).isTrue()
                }
            }
        }

        given("a coordinate outside the checkpoint") {
            val coordinate = Coordinate(700.0, 0.0)

            on("isInCheckpoint") {
                val inCheckpoint = checkpoint.isInCheckpoint(coordinate)

                it("should be false") {
                    assertThat(inCheckpoint).isFalse()
                }
            }
        }
    }
})