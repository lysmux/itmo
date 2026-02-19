package dev.lysmux.solver.ui

import com.varabyte.kotter.runtime.Session
import dev.lysmux.solver.domain.EquationSystem
import kotlin.random.Random

const val MIN = -10
const val MAX = 10

fun Session.generateRandom(): EquationSystem {
    val size = selectSize()
    val factors = Array(size) {
        DoubleArray(size) { Random.nextInt(MIN, MAX).toDouble() }
    }
    val rightFactors = DoubleArray(size) { Random.nextInt(MIN, MAX).toDouble() }

    return EquationSystem(
        size = size,
        factors = factors,
        rightFactors = rightFactors
    )
}