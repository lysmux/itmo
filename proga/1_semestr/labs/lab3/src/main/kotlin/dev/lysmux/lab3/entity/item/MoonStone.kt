package dev.lysmux.lab3.entity.item

import dev.lysmux.lab3.common.Case

class MoonStone : Item(){
    override fun caseDeclension(toCase: Case): String {
        return when (toCase) {
            Case.NOMINATIVE, Case.ACCUSATIVE -> "лунный камень"
            Case.GENITIVE -> "лунного камня"
            Case.DATIVE -> "лунному камню"
            Case.CREATIVE -> "лунным камнем"
            Case.PREPOSITIONAL -> "лунном камне"
        }
    }

    override fun getPluralName() = "лунные камни"

    override fun toString() = "лунный камень"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}