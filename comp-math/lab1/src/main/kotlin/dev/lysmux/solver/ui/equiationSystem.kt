package dev.lysmux.solver.ui

import com.varabyte.kotter.foundation.input.CharKey
import com.varabyte.kotter.foundation.input.Keys
import com.varabyte.kotter.foundation.input.onKeyPressed
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.runUntilSignal
import com.varabyte.kotter.foundation.text.black
import com.varabyte.kotter.foundation.text.bold
import com.varabyte.kotter.foundation.text.cyan
import com.varabyte.kotter.foundation.text.text
import com.varabyte.kotter.foundation.text.textLine
import com.varabyte.kotter.foundation.text.underline
import com.varabyte.kotter.runtime.Session
import com.varabyte.kotter.runtime.render.RenderScope
import dev.lysmux.solver.domain.EquationSystem
import dev.lysmux.solver.domain.formatCell
import dev.lysmux.solver.domain.matrix
import dev.lysmux.solver.domain.toEquationSystem
import kotlin.math.min
import kotlin.text.isDigit

const val COL_WIDTH = 8

fun Session.editEquationSystem(system: EquationSystem): EquationSystem {
    var curRow by liveVarOf(0)
    var curCol by liveVarOf(0)
    var cursor by liveVarOf(0)

    val matrix = system.matrix()

    var tempValue by liveVarOf(matrix[curRow][curCol].formatCell())

    section {
        matrix.forEachIndexed { rowIdx, row ->
            row.forEachIndexed { colIdx, col ->
                val isActive = curRow == rowIdx && curCol == colIdx
                val value = if (isActive) tempValue else col.formatCell()

                if (colIdx == matrix.lastIndex + 1) {
                    scopedState {
                        bold { text("| ") }
                    }
                }

                scopedState {
                    if (isActive) {
                        bold()
                        cyan()
                    }

                    val toShow = if (isActive) {
                        value.substring(
                            (cursor - COL_WIDTH + 1).coerceAtLeast(0),
                            (cursor + 1).coerceAtLeast(min(COL_WIDTH, value.length)).coerceAtMost(value.length)
                        ).padEnd(COL_WIDTH + 1)
                    } else value.take(COL_WIDTH).padEnd(COL_WIDTH + 1)

                    toShow.forEachIndexed { idx, char ->
                        scopedState {
                            if (isActive && idx == cursor.coerceAtMost(COL_WIDTH - 1)) underline()
                            text(char)
                        }
                    }
                }
            }
            textLine()
        }

        textLine()
        black(isBright = true) { textLine("Use UP/DOWN/LEFT/RIGHT to move") }
        textLine()
    }.runUntilSignal {
        onKeyPressed {
            when (key) {
                Keys.UP -> {
                    matrix[curRow][curCol] = tempValue.toDoubleOrNull() ?: 0.0

                    if (--curRow < 0) curRow = matrix.lastIndex - 1
                    tempValue = matrix[curRow][curCol].formatCell()
                    cursor = 0
                }

                Keys.DOWN -> {
                    matrix[curRow][curCol] = tempValue.toDoubleOrNull() ?: 0.0

                    if (++curRow > matrix.lastIndex) curRow = 0
                    tempValue = matrix[curRow][curCol].formatCell()
                    cursor = 0
                }

                Keys.RIGHT -> {
                    if (cursor < tempValue.length - 1) cursor++
                    else {
                        matrix[curRow][curCol] = tempValue.toDoubleOrNull() ?: 0.0

                        if (++curCol > matrix.lastIndex + 1) curCol = 0
                        tempValue = matrix[curRow][curCol].formatCell()
                        cursor = 0
                    }
                }

                Keys.LEFT -> {
                    if (cursor > 0) cursor--
                    else {
                        matrix[curRow][curCol] = tempValue.toDoubleOrNull() ?: 0.0

                        if (--curCol < 0) curCol = matrix.lastIndex + 1
                        tempValue = matrix[curRow][curCol].formatCell()
                        cursor = tempValue.length - 1
                    }
                }

                Keys.BACKSPACE -> {
                    if (tempValue.isEmpty()) return@onKeyPressed

                    if (cursor == 0) tempValue = tempValue.drop(1)
                    else {
                        tempValue = tempValue.dropAt(cursor)
                        cursor--
                    }
                }

                Keys.MINUS -> {
                    if (tempValue.isEmpty() || tempValue.all { it == '0' }) return@onKeyPressed
                    tempValue = if (tempValue.startsWith("-")) {
                        cursor--
                        tempValue.drop(1)
                    } else {
                        cursor++
                        "-$tempValue"
                    }
                }

                Keys.COMMA,
                Keys.PERIOD -> {
                    if (tempValue.isEmpty() || tempValue.contains('.')) return@onKeyPressed
                    tempValue = tempValue.insert(cursor + 1, ".")
                    cursor++
                }

                is CharKey -> {
                    val charKey = key as CharKey
                    if (!charKey.code.isDigit()) return@onKeyPressed

                    if (tempValue.all { it == '0' }) tempValue = charKey.code.toString()
                    else {
                        tempValue = tempValue.insert(cursor + 1, charKey.code.toString())
                        cursor++
                    }
                }

                Keys.ENTER -> {
                    matrix[curRow][curCol] = tempValue.toDoubleOrNull() ?: 0.0
                    signal()
                }
            }
            rerender()
        }
    }

    return matrix.toEquationSystem()
}

fun RenderScope.showMatrix(matrix: Array<DoubleArray>) {
    val space = (
            matrix.flatMap { it.asList() }
                .maxOfOrNull { it.formatCell().length }
                ?: COL_WIDTH
            ) + 1

    matrix.forEach { row ->
        row.forEachIndexed { colIdx, col ->
            if (colIdx == matrix.lastIndex + 1) {
                scopedState {
                    bold { text("| ") }
                }
            }

            text(col.formatCell().padEnd(space))
        }
        textLine()
    }
}

fun String.insert(index: Int, str: String) = this.substring(0, index) + str + this.substring(index)
fun String.dropAt(index: Int) = this.substring(0, index) + this.substring(index + 1)