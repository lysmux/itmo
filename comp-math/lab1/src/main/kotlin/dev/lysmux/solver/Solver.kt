package dev.lysmux.solver

import dev.lysmux.solver.domain.EquationSystem
import dev.lysmux.solver.domain.Solution
import dev.lysmux.solver.domain.SolveStatus
import dev.lysmux.solver.domain.matrix
import org.apache.commons.math3.linear.Array2DRowRealMatrix
import org.apache.commons.math3.linear.ArrayRealVector
import org.apache.commons.math3.linear.LUDecomposition

fun EquationSystem.solve(): Solution {
    val (triangularMatrix, determinant) = matrix().triangularize()
    val (triangularFactors, _) = factors.triangularize()

    if (triangularFactors.rank() < triangularMatrix.rank()) {
        return Solution(
            status = SolveStatus.NO_SOLUTION,
            triangleMatrix = triangularMatrix,
            determinant = determinant,
            answer = DoubleArray(rightFactors.size),
            discrepancy = DoubleArray(rightFactors.size),
        )
    }
    if (
        triangularFactors.rank() == triangularMatrix.rank()
        && triangularFactors.rank() != matrix().size
        ) {
        return Solution(
            status = SolveStatus.INFINITY_SOLUTION,
            triangleMatrix = triangularMatrix,
            determinant = determinant,
            answer = DoubleArray(rightFactors.size),
            discrepancy = DoubleArray(rightFactors.size),
        )
    }

    val answer = DoubleArray(rightFactors.size)
    triangularMatrix.indices.reversed().forEach {
        val sum = triangularMatrix.indices.reversed().sumOf { i ->
            triangularMatrix[it][i] * answer.getOrElse(i) { 0.0 }
        }
        val x = (triangularMatrix[it][triangularMatrix[it].size - 1] - sum) / triangularMatrix[it][it]
        answer[it] = x
    }

    val discrepancy = matrix().mapIndexed { rowIdx, row ->
        val res = row.take(row.size - 1).withIndex().sumOf { (colIdx, value) ->
            value * answer[colIdx]
        }
        res - matrix()[rowIdx][matrix()[rowIdx].size - 1]
    }.toDoubleArray()

    return Solution(
        status = SolveStatus.SOLVED,
        triangleMatrix = triangularMatrix,
        determinant = determinant,
        answer = answer,
        discrepancy = discrepancy
    )
}

fun Array<DoubleArray>.triangularize(): Pair<Array<DoubleArray>, Double> {
    val matrix = this.fullClone()
    var permutation = 0

    matrix.indices.forEach {
        if (matrix[it][it] == 0.0) {
            val newRowIdx = (it + 1 until matrix.size).firstOrNull {idx -> matrix[idx][it] != 0.0 }
            if (newRowIdx == null) return@forEach

            matrix[it] = matrix[newRowIdx].also {_ -> matrix[newRowIdx] = matrix[it] }
            permutation++
        }

        for (rowIdx in it + 1 until matrix.size) {
            val factor = (matrix[rowIdx][it] / matrix[it][it])
            if (factor != 0.0) {
                for (colIdx in it until matrix[rowIdx].size) {
                    matrix[rowIdx][colIdx] -= factor * matrix[it][colIdx]
                }
            }
        }

    }

    val sign = if (permutation % 2 == 0) 1.0 else -1.0
    val determinant = matrix.indices.fold(sign) { acc, i ->
        acc * matrix[i][i]
    }

    return matrix to determinant
}

fun Array<DoubleArray>.rank(): Int = this.count { row ->
    row.count { it != 0.0 } > 0
}

fun Array<DoubleArray>.fullClone(): Array<DoubleArray> = this.map { it.copyOf() }.toTypedArray()

fun Array<DoubleArray>.print() {
    this.forEach { println(it.contentToString()) }
}

fun EquationSystem.solveByLib(): DoubleArray? {
    val coefficients = Array2DRowRealMatrix(this.factors)
    val constants = ArrayRealVector(this.rightFactors)
    val solver = LUDecomposition(coefficients).solver

    return if (solver.isNonSingular) {
        val solution = solver.solve(constants)
        (0 until this.factors.size).map {
            solution.getEntry(it)
        }.toDoubleArray()
    } else null
}

fun main() {
    val matrix = arrayOf(
        doubleArrayOf(2.0, 1.0, -1.0, 8.0),
        doubleArrayOf(-3.0, -1.0, 2.0, -11.0),
        doubleArrayOf(-2.0, 1.0, 2.0, -3.0),
    )

    matrix.triangularize().first.print()
}