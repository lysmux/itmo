package dev.lysmux.lab3.entity.ability

import dev.lysmux.lab3.entity.ray.Ray

class EmitRays(private val emmitedRay: Ray, private vararg val influencingRays: Ray) : Ability() {
    private fun influenced(): String {
        val rayKindNames = influencingRays.map { it.kind.value }
        val builder = StringBuilder("под влиянием ")

        when (rayKindNames.size) {
            1 -> builder.append(rayKindNames[0])
            2 -> builder.append("${rayKindNames[0]} или ${rayKindNames[1]}")
            else -> {
                builder.append(rayKindNames.subList(0, rayKindNames.size - 1).joinToString(", "))
                builder.append(" или ${rayKindNames.last()}")
            }
        }
        builder.append(" лучей")
        return builder.toString()
    }

    override fun getDescription(): String {
        return "испускать ${emmitedRay.visibility.value} ${emmitedRay.getPluralName()} ${
            emmitedRay.kind.value
        } даже ${influenced()}"
    }

    override fun toString() = "испускать лучи"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EmitRays) return false

        if (emmitedRay != other.emmitedRay) return false
        if (!influencingRays.contentEquals(other.influencingRays)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = emmitedRay.hashCode()
        result = 31 * result + influencingRays.contentHashCode()
        return result
    }
}