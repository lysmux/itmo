package dev.lysmux.solver.ui

import com.varabyte.kotter.runtime.Session
import dev.lysmux.solver.domain.EquationSystem

fun Session.fromRaw(): EquationSystem {
    val size = selectSize()
    return editEquationSystem(EquationSystem(size))
}