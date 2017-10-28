package org.neige.codingame.codersstrikeback

import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on

object VectorSpec : Spek({

    given("a vector 100;100") {
        val u = Scalar(100.0, 100.0)

        on("check angle") {
            val angle = u.angle()
            it("should be 45°") {
                assertThat(angle).isEqualTo(45.0)
            }
        }

        on("inverse") {
            val inverse = u.inverse()
            it("should be -100;-100") {
                assertThat(inverse).isEqualTo(Scalar(-100.0, -100.0))
            }
        }

        on("add friction") {
            val v = u * 0.5
            it("should be reduce by half") {
                assertThat(v).isEqualTo(Scalar(50.0, 50.0))
            }
        }

        on("divide in 100") {
            val v = u / 100.0
            it("should be a 1;1 vector") {
                assertThat(v).isEqualTo(Scalar(1.0, 1.0))
            }
        }

        given("a vector -100;-100") {
            val v = Scalar(-100.0, -100.0)

            on("addition") {
                val vector = u + v
                it("should be null") {
                    assertThat(vector).isEqualTo(Scalar(0.0, 0.0))
                }
            }

            on("subtraction") {
                val subtraction = u - v
                it("should be equals to 200;200") {
                    assertThat(subtraction).isEqualTo(Scalar(200.0, 200.0))
                }

                on("absolute magnitude") {
                    val strength = subtraction.magnitude()
                    it("should be 400") {
                        assertThat(strength).isEqualTo(400.0)
                    }
                }

            }

            on("absolute magnitude") {
                val strength = u.magnitude()
                it("should be 141") {
                    assertThat(strength).isEqualTo(141.4213562373095)
                }
            }

            on("angle") {
                val angle = u.angle(v)
                it("should be 180°") {
                    assertThat(angle).isEqualTo(180.0)
                }
            }
        }

        given("a vector 100;100") {
            val v = Scalar(100.0, 100.0)

            on("addition") {
                val vector = u + v
                it("should be 200;200") {
                    assertThat(vector).isEqualTo(Scalar(200.0, 200.0))
                }
            }

            on("subtraction") {
                val subtraction = u - v
                it("should be equals to 0;0") {
                    assertThat(subtraction).isEqualTo(Scalar(0.0, 0.0))
                }

                on("absolute magnitude") {
                    val strength = subtraction.magnitude()
                    it("should be 0") {
                        assertThat(strength).isEqualTo(0.0)
                    }
                }
            }
        }

        given("a vector 100;-100") {
            val v = Scalar(100.0, -100.0)

            on("angle") {
                val angle = u.angle(v)
                it("should be 90°") {
                    assertThat(angle).isEqualTo(90.0)
                }
            }
        }

    }

    given("a vector 0;100") {
        val u = Scalar(0.0, 100.0)

        on("check angle") {
            val angle = u.angle()
            it("should be 90°") {
                assertThat(angle).isEqualTo(90.0)
            }
        }
    }

    given("a vector 0;-100") {
        val u = Scalar(0.0, -100.0)

        on("check angle") {
            val angle = u.angle()
            it("should be -90°") {
                assertThat(angle).isEqualTo(-90.0)
            }
        }
    }

    given("a vector 100;0") {
        val u = Scalar(100.0, 0.0)

        on("check angle") {
            val angle = u.angle()
            it("should be 0°") {
                assertThat(angle).isEqualTo(0.0)
            }
        }
    }

    given("a vector -100;0") {
        val u = Scalar(-100.0, 0.0)

        on("check angle") {
            val angle = u.angle()
            it("should be 0°") {
                assertThat(angle).isEqualTo(-0.0)
            }
        }
    }

    given("two point") {
        val a = Coordinate(-4.0, -3.0)
        val b = Coordinate(1.0, 5.0)
        on("construction") {
            val v = Scalar(a, b)
            it("should be equals to 5;8") {
                assertThat(v).isEqualTo(Scalar(5.0, 8.0))
            }
        }
    }

})