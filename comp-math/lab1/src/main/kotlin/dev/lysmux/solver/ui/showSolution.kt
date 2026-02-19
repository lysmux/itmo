package dev.lysmux.solver.ui

import com.varabyte.kotter.foundation.text.*
import com.varabyte.kotter.runtime.Session
import com.varabyte.kotter.runtime.render.RenderScope
import dev.lysmux.solver.domain.Solution
import dev.lysmux.solver.domain.SolveStatus
import dev.lysmux.solver.domain.formatCell

fun Session.showSolution(solution: Solution) = section {
    green {
        bold {
            textLine("Solution:")
        }
    }

    cyan {
        bold {
            textLine("Triangle matrix:")
        }
    }
    showMatrix(solution.triangleMatrix)
    textLine()

    when (solution.status) {
        SolveStatus.SOLVED -> {
            cyan {
                bold {
                    textLine("Determinant:")
                }
            }
            textLine(solution.determinant.toString())
            textLine()

            cyan {
                bold {
                    textLine("Answer:")
                }
            }
            printVector(solution.answer)
            textLine()

            cyan {
                bold {
                    textLine("Discrepancy:")
                }
            }
            printVector(solution.discrepancy)
        }
        SolveStatus.INFINITY_SOLUTION -> {
            red {
                bold {
                    textLine("Equation system has infinity solution")
                }
            }
        }
        SolveStatus.NO_SOLUTION -> {
            red {
                bold {
                    textLine("Equation system has not solution")
                }
            }
        }
    }
}.run { }

fun RenderScope.printVector(vector: DoubleArray) {
    text("[")
    vector.forEachIndexed { idx, value ->
        text(value.formatCell())
        if (idx != vector.lastIndex) text(", ")
    }
    text("]")
}