package dev.lysmux.lab3.entity.ability

import dev.lysmux.lab3.entity.ray.Ray

class EmitRays(private val emmitedRay: Ray, private vararg val influencingRays: Ray) : Ability(){
    fun influenced(): String {
        val rayKindNames = influencingRays.map { it.getKind().getValue() }

        return "под влиянием " + when (rayKindNames.size) {
            1 -> rayKindNames[0]
            2 -> "${rayKindNames[0]} или ${rayKindNames[1]}"
            else -> rayKindNames.subList(0, rayKindNames.size - 1).joinToString(", ") + " или ${rayKindNames.last()}"
        } + " лучей"
    }

    override fun getDescription() = "испускать ${emmitedRay.getVisibility().getValue()} ${emmitedRay.getPluralName()} ${emmitedRay.getKind().getValue()} даже ${influenced()}"

    override fun toString() = "испускать лучи"
}