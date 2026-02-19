package dev.lysmux.solver.domain

data class Solution(
    val status: SolveStatus,
    val triangleMatrix: Array<DoubleArray>,
    val determinant: Double,
    val answer: DoubleArray,
    val discrepancy: DoubleArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Solution

        if (determinant != other.determinant) return false
        if (status != other.status) return false
        if (!triangleMatrix.contentDeepEquals(other.triangleMatrix)) return false
        if (!answer.contentEquals(other.answer)) return false
        if (!discrepancy.contentEquals(other.discrepancy)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = determinant.hashCode()
        result = 31 * result + status.hashCode()
        result = 31 * result + triangleMatrix.contentDeepHashCode()
        result = 31 * result + answer.contentHashCode()
        result = 31 * result + discrepancy.contentHashCode()
        return result
    }

}