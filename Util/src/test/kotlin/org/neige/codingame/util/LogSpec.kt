package org.neige.codingame.util

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.io.PrintStream


object LogSpec : Spek({

    lateinit var err: ByteArrayOutputStream
    val originalErr = System.err

    beforeEachTest {
        err = ByteArrayOutputStream()
        System.setErr(PrintStream(err))
    }

    beforeEachGroup {
        System.setErr(originalErr)
    }

    describe("a log object") {
        val log by memoized { Log }

        describe("not loggable") {
            beforeEachTest {
                log.loggable = false
                log.debug("Hello")
            }

            it("shouldn't log string") {
                assertThat(err.toString()).isEmpty()
            }
        }

        describe("not loggable") {
            beforeEachTest { log.loggable = true }

            describe("log Hello") {
                beforeEachTest {
                    log.debug("Hello")
                }

                it("should log string") {
                    assertThat("Hello\n").isEqualTo(err.toString())
                }
            }

            describe("Log array of array") {
                val board = Array(2) { x -> Array(2) { y -> "[$x, $y]" } }
                beforeEachTest { log.debug(board) { it } }

                it("should print the board") {
                    assertThat("[0, 0][1, 0]\n[0, 1][1, 1]\n").isEqualTo(err.toString())
                }
            }
        }
    }
})
