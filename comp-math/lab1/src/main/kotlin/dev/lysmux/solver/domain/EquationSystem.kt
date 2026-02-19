package dev.lysmux.solver.domain

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.collections.plus

const val MAX_SIZE = 20

@Serializable
data class EquationSystem(
    val size: Int,
    val factors: Array<DoubleArray>,
    val rightFactors: DoubleArray,
) {
    constructor(size: Int) : this(
        size = size,
        factors = Array(size) { DoubleArray(size) { 0.0 } },
        rightFactors = DoubleArray(size) { 0.0 }
    )

    init {
        require(size > 0) { "Size must be positive" }
        require(size <= MAX_SIZE) { "Size must be less than $MAX_SIZE" }
        require(factors.all { it.size == size }) { "Factors must have the same size as the equation system" }
        require(factors.size == rightFactors.size) { "Factors and right factors must have the same size" }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EquationSystem

        if (size != other.size) return false
        if (!factors.contentDeepEquals(other.factors)) return false
        if (!rightFactors.contentEquals(other.rightFactors)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = size
        result = 31 * result + factors.contentDeepHashCode()
        result = 31 * result + rightFactors.contentHashCode()
        return result
    }
}


fun EquationSystem.matrix(): Array<DoubleArray> {
    return factors.mapIndexed { idx, row ->
        row + rightFactors[idx]
    }.toTypedArray()
}

fun Array<DoubleArray>.toEquationSystem(): EquationSystem {
    return EquationSystem(
        size = this.size,
        factors = this.map { it.dropLast(1).toDoubleArray() }.toTypedArray(),
        rightFactors = this.map { it.last() }.toDoubleArray()
    )
}

fun File.toEquationSystem(): EquationSystem {
    if (!this.exists()) throw IllegalArgumentException("File not found")

    return try {
        Json.decodeFromString(this.readText())
    } catch (_: SerializationException) {
        throw IllegalArgumentException("Could not parse file as equation system")
    }
}

fun Double.formatCell(): String {
    return if (this % 1.0 == 0.0) {
        this.toLong().toString()
    } else {
        this.toBigDecimal().toPlainString()
    }
}