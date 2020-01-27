package org.neige.codingame.util

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.PrintStream


object LogSpec : Spek({

    lateinit var err: ByteArrayOutputStream
    var originalErr = System.err

    beforeEachTest {
        err = ByteArrayOutputStream()
        System.setErr(PrintStream(err))
    }

    beforeEachGroup {
        System.setErr(originalErr)
    }

    describe("a log object") {
        val log by memoized { Log }

        describe("log Hello") {
            beforeEachTest {
                log.debug("Hello")
            }

            it("should log string") {
                assertThat("Hello\n").isEqualTo(err.toString())
            }
        }

        describe("log 11") {
            beforeEachTest {
                log.debug(11)
            }

            it("should log int") {
                assertThat("11\n").isEqualTo(err.toString())
            }
        }

        describe("log c") {
            beforeEachTest {
                log.debug('c')
            }

            it("should log char") {
                assertThat("c\n").isEqualTo(err.toString())
            }
        }
    }

})
