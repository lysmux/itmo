package dev.lysmux.lab3.entity.place

import dev.lysmux.lab3.common.Case

class Table : Place() {
    override fun caseDeclension(toCase: Case): String {
        return when (toCase) {
            Case.NOMINATIVE -> "стол"
            Case.GENITIVE -> "стола"
            Case.DATIVE -> "столу"
            Case.ACCUSATIVE -> "стол"
            Case.CREATIVE -> "столом"
            Case.PREPOSITIONAL -> "столе"
        }
    }

    override fun toString() = "стол"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is Table
    }

    override fun hashCode() = javaClass.hashCode()
}