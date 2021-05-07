package org.neige.codingame.totoro


/**
 * [0-1] coefficient day
 * [0-1] coefficient action
 * [0-1] coefficient efficiency
 */
class Score {

    var dayCoefficient: Double = 0.0
        set(value) {
            assert(value in 0.0..1.0)
            field = value
        }
    var actionCoefficient: Double = 0.0
        set(value) {
            assert(value in 0.0..1.0)
            field = value
        }
    var efficiencyCoefficient = 0.0
        set(value) {
            assert(value in 0.0..1.0)
            field = value
        }

    val score: Double
        get() {
            return dayCoefficient + (actionCoefficient + efficiencyCoefficient)
        }

}
