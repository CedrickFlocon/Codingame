package org.neige.codingame.util

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.io.PrintStream


object ScannerLoggableSpec : Spek({

    lateinit var err: ByteArrayOutputStream
    val originalErr = System.err

    beforeEachTest {
        err = ByteArrayOutputStream()
        System.setErr(PrintStream(err))
    }

    beforeEachGroup {
        System.setErr(originalErr)
    }

    describe("a scanner") {
        val scanner by memoized { ScannerLoggable(this::class.java.classLoader.getResourceAsStream("input.txt")!!) }

        describe("log Hello") {
            lateinit var firstLine: String
            lateinit var secondine: String
            lateinit var thirdLine: String
            beforeEachTest {
                firstLine = scanner.nextLine()
                secondine = scanner.nextLine()
                thirdLine = scanner.nextLine()
            }

            it("should have the data") {
                assertThat(firstLine).isEqualTo("35 13")
                assertThat(secondine).isEqualTo("#####")
                assertThat(thirdLine).isEqualTo("12")
            }

            it("should log string") {
                assertThat("35 13\n" +
                        "#####\n" +
                        "12\n").isEqualTo(err.toString())
            }
        }
    }

})
