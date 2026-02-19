package dev.lysmux.solver

import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.bold
import com.varabyte.kotter.foundation.text.green
import com.varabyte.kotter.foundation.text.red
import com.varabyte.kotter.foundation.text.textLine
import dev.lysmux.solver.domain.matrix
import dev.lysmux.solver.ui.*
import dev.lysmux.solver.ui.fromFile
import dev.lysmux.solver.ui.fromRaw
import dev.lysmux.solver.ui.generateRandom
import dev.lysmux.solver.ui.printVector
import dev.lysmux.solver.ui.selectMode
import dev.lysmux.solver.ui.showMatrix
import dev.lysmux.solver.ui.showSolution

fun loop() = session {
    val mode = selectMode()
    val system = when (mode) {
        Mode.FILE -> fromFile()
        Mode.RAW -> fromRaw()
        Mode.RANDOM -> generateRandom()
    }

    if (mode != Mode.RAW) {
        section {
            green {
                bold {
                    textLine("Equation system:")
                }
            }
            showMatrix(system.matrix())
            textLine()
        }.run { }
    }

    val solution = system.solve()
    showSolution(solution)

    val librarySolution = system.solveByLib()
    section {
        textLine()

        green {
            bold {
                textLine("Library solution:")
            }
        }

        if (librarySolution != null) {
            printVector(librarySolution)
        } else {
            red {
                bold {
                    textLine("Matrix has no solution, or there are infinitely many of them")
                }
            }
        }
    }.run { }
}

fun main() {
    while (true) loop()
}