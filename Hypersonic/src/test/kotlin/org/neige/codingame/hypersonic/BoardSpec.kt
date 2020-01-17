package org.neige.codingame.hypersonic

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import java.util.*


object BoardSpec : Spek({

    given("a board with simple element") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_game_simple_element.txt"))
        val board = Board(scanner, 13, 11)

        on("first turn") {
            board.nextTurn()

            it("should add box") {
                assertThat(board.getGridElement(Coordinate(0, 0))).isEqualTo(Box(0, 0, ItemType.NONE))
                assertThat(board.getGridElement(Coordinate(12, 10))).isEqualTo(Box(12, 10, ItemType.NONE))
            }

            it("should add box with item extra range") {
                assertThat(board.getGridElement(Coordinate(4, 3))).isEqualTo(Box(4, 3, ItemType.EXTRA_RANGE))
                assertThat(board.getGridElement(Coordinate(7, 9))).isEqualTo(Box(7, 9, ItemType.EXTRA_RANGE))
            }

            it("should add box with item extra bomb") {
                assertThat(board.getGridElement(Coordinate(8, 1))).isEqualTo(Box(8, 1, ItemType.EXTRA_BOMB))
                assertThat(board.getGridElement(Coordinate(7, 5))).isEqualTo(Box(7, 5, ItemType.EXTRA_BOMB))
            }

            it("should add item extra range") {
                assertThat(board.getGridElement(Coordinate(3, 3))).isEqualTo(Item(3, 3, ItemType.EXTRA_RANGE))
                assertThat(board.getGridElement(Coordinate(5, 3))).isEqualTo(Item(5, 3, ItemType.EXTRA_RANGE))
            }

            it("should add item extra range") {
                assertThat(board.getGridElement(Coordinate(5, 5))).isEqualTo(Item(5, 5, ItemType.EXTRA_BOMB))
                assertThat(board.getGridElement(Coordinate(2, 3))).isEqualTo(Item(2, 3, ItemType.EXTRA_BOMB))
            }

            it("should add wall") {
                assertThat(board.getGridElement(Coordinate(9, 3))).isEqualTo(Wall(9, 3))
                assertThat(board.getGridElement(Coordinate(4, 7))).isEqualTo(Wall(4, 7))
            }
        }

        on("second turn") {
            board.nextTurn()

            it("should clear removed box") {
                assertThat(board.getGridElement(Coordinate(0, 0))).isEqualTo(Box(0, 0, ItemType.NONE))
                assertThat(board.getGridElement(Coordinate(12, 10))).isEqualTo(Floor(12, 10))
            }

            it("should clear removed box with item extra range") {
                assertThat(board.getGridElement(Coordinate(4, 3))).isEqualTo(Floor(4, 3))
                assertThat(board.getGridElement(Coordinate(7, 9))).isEqualTo(Box(7, 9, ItemType.EXTRA_RANGE))
            }

            it("should clear removed box with item extra bomb") {
                assertThat(board.getGridElement(Coordinate(8, 1))).isEqualTo(Box(8, 1, ItemType.EXTRA_BOMB))
                assertThat(board.getGridElement(Coordinate(7, 5))).isEqualTo(Floor(7, 5))
            }

            it("should keep wall") {
                assertThat(board.getGridElement(Coordinate(9, 3))).isEqualTo(Wall(9, 3))
                assertThat(board.getGridElement(Coordinate(4, 7))).isEqualTo(Wall(4, 7))
            }
        }
    }

    given("a board with simple player") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_game_simple_player.txt"))
        val board = Board(scanner, 13, 11)

        on("first turn") {
            board.nextTurn()

            it("should have player zero") {
                assertThat(board.getPlayer(0)).isEqualTo(Player(0, 0, 0, 1, 3))
            }

            it("should have player one") {
                assertThat(board.getPlayer(1)).isEqualTo(Player(1, 12, 10, 2, 5))
            }
        }

        on("second turn") {
            board.nextTurn()

            it("should have move player zero") {
                assertThat(board.getPlayer(0)).isEqualTo(Player(0, 1, 0, 1, 3))
            }

            it("should have move player one") {
                assertThat(board.getPlayer(1)).isEqualTo(Player(1, 11, 10, 2, 5))
            }
        }
    }

    given("a board with simple bomb") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_game_simple_bomb.txt"))
        val board = Board(scanner, 13, 11)

        on("first turn with one bomb") {
            board.nextTurn()

            it("should have a bomb in 5;5") {
                assertThat(board.getGridElement(Coordinate(5, 5))).isEqualTo(Bomb(0, 5, 5, 5, 3))
            }

            it("should explode 5;5") {
                assertThat(board.timerToExplode(Coordinate(5, 5))).isEqualTo(5)
            }

            it("should explode horizontally") {
                assertThat(board.timerToExplode(Coordinate(7, 5))).isEqualTo(5)
                assertThat(board.timerToExplode(Coordinate(6, 5))).isEqualTo(5)
                assertThat(board.timerToExplode(Coordinate(4, 5))).isEqualTo(5)
                assertThat(board.timerToExplode(Coordinate(3, 5))).isEqualTo(5)
            }

            it("should explode vertically") {
                assertThat(board.timerToExplode(Coordinate(5, 7))).isEqualTo(5)
                assertThat(board.timerToExplode(Coordinate(5, 6))).isEqualTo(5)
                assertThat(board.timerToExplode(Coordinate(5, 4))).isEqualTo(5)
                assertThat(board.timerToExplode(Coordinate(5, 3))).isEqualTo(5)
            }

            it("should not explode outside the range") {
                assertThat(board.timerToExplode(Coordinate(5, 8))).isEqualTo(null)
                assertThat(board.timerToExplode(Coordinate(5, 2))).isEqualTo(null)
                assertThat(board.timerToExplode(Coordinate(8, 5))).isEqualTo(null)
                assertThat(board.timerToExplode(Coordinate(2, 5))).isEqualTo(null)
            }
        }

        on("second turn without bomb") {
            board.nextTurn()

            it("should remove the bomb") {
                assertThat(board.getGridElement(Coordinate(5, 5))).isEqualTo(Floor(5, 5))
            }
        }

        on("third turn with box and wall") {
            board.nextTurn()

            it("should explode 5;5") {
                assertThat(board.timerToExplode(Coordinate(5, 5))).isEqualTo(2)
            }

            it("should explode before element horizontally") {
                assertThat(board.timerToExplode(Coordinate(2, 5))).isEqualTo(2)
                assertThat(board.timerToExplode(Coordinate(3, 5))).isEqualTo(2)
                assertThat(board.timerToExplode(Coordinate(4, 5))).isEqualTo(2)
                assertThat(board.timerToExplode(Coordinate(6, 5))).isEqualTo(2)
                assertThat(board.timerToExplode(Coordinate(7, 5))).isEqualTo(2)
            }

            it("should explode vertically") {
                assertThat(board.timerToExplode(Coordinate(5, 6))).isEqualTo(2)
                assertThat(board.timerToExplode(Coordinate(5, 4))).isEqualTo(2)
            }

            it("should explode a box") {
                assertThat(board.timerToExplode(Coordinate(5, 3))).isEqualTo(2)
                assertThat(board.timerToExplode(Coordinate(8, 5))).isEqualTo(2)
            }

            it("should explode a wall") {
                assertThat(board.timerToExplode(Coordinate(5, 7))).isEqualTo(2)
                assertThat(board.timerToExplode(Coordinate(1, 5))).isEqualTo(2)
            }

            it("should not explode after a box or a wall") {
                assertThat(board.timerToExplode(Coordinate(0, 5))).isEqualTo(null)
                assertThat(board.timerToExplode(Coordinate(0, 9))).isEqualTo(null)
                assertThat(board.timerToExplode(Coordinate(5, 2))).isEqualTo(null)
                assertThat(board.timerToExplode(Coordinate(5, 8))).isEqualTo(null)
            }
        }

        on("fourth turn with item") {
            board.nextTurn()

            it("should explode 5;5") {
                assertThat(board.timerToExplode(Coordinate(5, 5))).isEqualTo(7)
            }

            it("should explode before element horizontally") {
                assertThat(board.timerToExplode(Coordinate(2, 5))).isEqualTo(7)
                assertThat(board.timerToExplode(Coordinate(3, 5))).isEqualTo(7)
                assertThat(board.timerToExplode(Coordinate(4, 5))).isEqualTo(7)
                assertThat(board.timerToExplode(Coordinate(6, 5))).isEqualTo(7)
                assertThat(board.timerToExplode(Coordinate(7, 5))).isEqualTo(7)
            }

            it("should explode vertically") {
                assertThat(board.timerToExplode(Coordinate(5, 6))).isEqualTo(7)
                assertThat(board.timerToExplode(Coordinate(5, 4))).isEqualTo(7)
            }

            it("should explode a item") {
                assertThat(board.timerToExplode(Coordinate(5, 3))).isEqualTo(7)
                assertThat(board.timerToExplode(Coordinate(5, 7))).isEqualTo(7)
                assertThat(board.timerToExplode(Coordinate(1, 5))).isEqualTo(7)
                assertThat(board.timerToExplode(Coordinate(8, 5))).isEqualTo(7)
            }

            it("should not explode after an item") {
                assertThat(board.timerToExplode(Coordinate(0, 5))).isEqualTo(null)
                assertThat(board.timerToExplode(Coordinate(9, 5))).isEqualTo(null)
                assertThat(board.timerToExplode(Coordinate(5, 2))).isEqualTo(null)
                assertThat(board.timerToExplode(Coordinate(5, 8))).isEqualTo(null)
            }
        }

        on("fifth turn with 2 bomb") {
            board.nextTurn()

            it("should have the min timer") {
                assertThat(board.timerToExplode(Coordinate(3, 3))).isEqualTo(3)
                assertThat(board.timerToExplode(Coordinate(5, 3))).isEqualTo(3)
                assertThat(board.timerToExplode(Coordinate(4, 3))).isEqualTo(3)
                assertThat(board.timerToExplode(Coordinate(3, 6))).isEqualTo(3)
            }

        }
    }

    given("a board with game action") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_game_simple_action.txt"))
        val board = Board(scanner, 13, 11)

        given("first turn without bomb") {
            beforeGroup {
                board.nextTurn()
            }

            on("get closest box of 2;2") {
                val box = board.getClosestBox(Coordinate(2, 2))
                it("should return box 1;0") {
                    assertThat(box).isEqualTo(Box(1, 0, ItemType.NONE))
                }
            }

            on("get closest box of 12;11") {
                val box = board.getClosestBox(Coordinate(12, 10))
                it("should return box 9;5") {
                    assertThat(box).isEqualTo(Box(9, 5, ItemType.EXTRA_RANGE))
                }
            }
        }

        given("second turn with bomb") {
            beforeGroup {
                board.nextTurn()
            }

            on("get closest box of 2;2") {
                val box = board.getClosestBox(Coordinate(2, 2))
                it("should return box 4;5") {
                    assertThat(box).isEqualTo(Box(4, 5, ItemType.NONE))
                }
            }
        }
    }

    given("a board with blocked path") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_game_accessible_path.txt"))
        val board = Board(scanner, 13, 11)

        on("get accessible path") {
            board.nextTurn()
            val accessiblePath = board.getAccessiblePath(Player(0, 5, 5, 0, 0))

            it("should have only accessible path") {
                assertThat(accessiblePath).hasSize(6)
                assertThat(accessiblePath).contains(Floor(5, 5))
                assertThat(accessiblePath).contains(Floor(5, 4))
                assertThat(accessiblePath).contains(Floor(6, 4))
                assertThat(accessiblePath).contains(Floor(6, 3))
                assertThat(accessiblePath).contains(Floor(5, 3))
                assertThat(accessiblePath).contains(Floor(4, 3))
            }
        }

        on("accessible path on bomb") {
            board.nextTurn()
            val accessiblePath = board.getAccessiblePath(Player(0, 5, 5, 0, 0))

            it("should have only accessible path") {
                assertThat(accessiblePath).hasSize(6)
                assertThat(accessiblePath).contains(Floor(5, 4))
                assertThat(accessiblePath).contains(Floor(6, 4))
                assertThat(accessiblePath).contains(Floor(6, 3))
                assertThat(accessiblePath).contains(Floor(5, 3))
                assertThat(accessiblePath).contains(Floor(4, 3))
            }
        }

    }

    given("a board to build path") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_game_path.txt"))
        val board = Board(scanner, 13, 11)

        on("build path to") {
            board.nextTurn()
            val buildPathTo = board.buildPathTo(Coordinate(5, 4), Coordinate(4, 1))

            it("should have only two path") {
                assertThat(buildPathTo).hasSize(2)
            }

            it("should have one path with 3 spot") {
                assertThat(buildPathTo[0]).hasSize(7)
                assertThat(buildPathTo[0][buildPathTo[0].size - 1].sameLocated(Coordinate(4, 1))).isTrue()

            }

            it("should have one path with 5 spot") {
                assertThat(buildPathTo[1]).hasSize(5)
                assertThat(buildPathTo[1][buildPathTo[1].size - 1].sameLocated(Coordinate(4, 1))).isTrue()
            }
        }
    }

    given("a board to find a safe place") {
        val scanner = Scanner(this::class.java.classLoader.getResourceAsStream("input_game_safe_place.txt"))
        val board = Board(scanner, 13, 11)

        on("get safe place") {
            board.nextTurn()
            val safePlace = board.getClosestSafePlace(Player(0, 0, 1, 0, 0))

            it("should be coordinate 1;0") {
                assertThat(safePlace?.x).isEqualTo(1)
                assertThat(safePlace?.y).isEqualTo(0)
            }
        }
    }

})