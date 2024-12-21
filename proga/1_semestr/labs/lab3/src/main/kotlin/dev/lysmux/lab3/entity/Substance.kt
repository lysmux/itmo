package dev.lysmux.lab3.entity

import dev.lysmux.lab3.common.Case
import dev.lysmux.lab3.common.HasCase
import dev.lysmux.lab3.common.Pluralable
import dev.lysmux.lab3.entity.ability.Ability
import dev.lysmux.lab3.entity.place.Place
import dev.lysmux.lab3.entity.ray.Ray

class Substance(private val ability: Ability) : Pluralable, HasCase {
    override fun getPluralName() = "вещества"

    override fun toString() = "вещество"

    override fun caseDeclension(toCase: Case): String {
        return when (toCase) {
            Case.NOMINATIVE -> "вещество"
            Case.GENITIVE -> "вещества"
            Case.DATIVE -> "веществу"
            Case.ACCUSATIVE -> "вещество"
            Case.CREATIVE -> "веществом"
            Case.PREPOSITIONAL -> "веществе"
        }
    }

    fun meet(place: Place) = "в ${place.caseDeclension(Case.PREPOSITIONAL)} встречаются ${getPluralName()}"

    fun influenced(ray: Ray) = "подвергнутся действию ${ray.caseDeclension(Case.GENITIVE)} ${ray.kind.value}"

    fun acquireAbility() = "приобретают способность ${ability.getDescription()}"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Substance) return false
        return ability == other.ability
    }

    override fun hashCode() = ability.hashCode()
}