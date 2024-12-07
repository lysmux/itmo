package dev.lysmux.lab3.entity.place

import dev.lysmux.lab3.common.Case

class Nature : Place(){
    override fun caseDeclension(toCase: Case): String {
        return when (toCase) {
            Case.NOMINATIVE -> "природа"
            Case.PREPOSITIONAL -> "природе"
            Case.GENITIVE -> "природы"
            Case.DATIVE -> "природе"
            Case.ACCUSATIVE -> "природу"
            Case.CREATIVE -> "природой"
        }
    }

    override fun toString() = "природа"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}