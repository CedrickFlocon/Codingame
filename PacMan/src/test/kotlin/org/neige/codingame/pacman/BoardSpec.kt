package org.neige.codingame.pacman

import org.assertj.core.api.Assertions.assertThat
import org.neige.codingame.geometry.Coordinate
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.util.*

object BoardSpec : Spek({

    val board by memoized { Board(Board.readGrid(Scanner(this::class.java.classLoader.getResourceAsStream("board/board_seed_-7178209629524502500.txt")))) }

    describe("coordinate") {
        val coordinate by memoized { Coordinate(25, 5) }

        describe("build path") {
            lateinit var paths: List<List<Coordinate>>
            beforeEachTest {
                paths = board.buildPath(coordinate, 5)
            }

            it("should found all the path") {
                assertThat(paths).containsAll(listOf(
                        listOf(Coordinate(25, 4), Coordinate(25, 3), Coordinate(24, 3), Coordinate(23, 3), Coordinate(22, 3)),
                        listOf(Coordinate(25, 4), Coordinate(25, 3), Coordinate(24, 3), Coordinate(23, 3), Coordinate(23, 4)),
                        listOf(Coordinate(25, 4), Coordinate(25, 3), Coordinate(26, 3), Coordinate(27, 3), Coordinate(27, 2)),
                        listOf(Coordinate(26, 5), Coordinate(27, 5), Coordinate(28, 5), Coordinate(29, 5), Coordinate(30, 5)),
                        listOf(Coordinate(26, 5), Coordinate(27, 5), Coordinate(27, 6), Coordinate(27, 7), Coordinate(26, 7)),
                        listOf(Coordinate(26, 5), Coordinate(27, 5), Coordinate(27, 6), Coordinate(27, 7), Coordinate(27, 8)),
                        listOf(Coordinate(25, 6), Coordinate(25, 7), Coordinate(26, 7), Coordinate(27, 7), Coordinate(27, 6)),
                        listOf(Coordinate(25, 6), Coordinate(25, 7), Coordinate(26, 7), Coordinate(27, 7), Coordinate(27, 8)),
                        listOf(Coordinate(25, 6), Coordinate(25, 7), Coordinate(25, 8), Coordinate(25, 9), Coordinate(24, 9)),
                        listOf(Coordinate(25, 6), Coordinate(25, 7), Coordinate(25, 8), Coordinate(25, 9), Coordinate(25, 10)),
                        listOf(Coordinate(25, 4), Coordinate(25, 3), Coordinate(25, 2), Coordinate(25, 1)))
                )
            }
        }

    }

})