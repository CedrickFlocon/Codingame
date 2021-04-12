package org.neige.codingame.util

import org.assertj.core.api.Assertions.assertThat
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.io.ByteArrayOutputStream
import java.io.PrintStream


object ScannerLoggableSpec : Spek({

    val err by memoized { ByteArrayOutputStream() }
    val originalErr = System.err

    beforeEachTest { System.setErr(PrintStream(err)) }
    beforeEachGroup { System.setErr(originalErr) }

    describe("a scanner") {
        val scanner by memoized { ScannerLoggable(this::class.java.classLoader.getResourceAsStream("input.txt")!!) }

        describe("next stuff") {
            it("should have data") {
                assertThat(scanner.nextInt()).isEqualTo(35)
                assertThat(scanner.nextInt()).isEqualTo(13)
                assertThat(scanner.next()).isEqualTo("newline")
                assertThat(scanner.nextLine()).isEqualTo("")
                assertThat(scanner.nextLine()).isEqualTo("#####")
                assertThat(scanner.nextInt()).isEqualTo(12)

                assertThat("35\n13\n" +
                        "newline\n" +
                        "\n" +
                        "#####\n" +
                        "12\n").isEqualTo(err.toString())
            }
        }
    }

})
