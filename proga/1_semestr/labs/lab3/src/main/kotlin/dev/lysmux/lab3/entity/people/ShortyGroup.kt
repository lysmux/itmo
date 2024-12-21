package dev.lysmux.lab3.entity.people

import dev.lysmux.lab3.common.Case
import dev.lysmux.lab3.common.HasCase

class ShortyGroup(private var state: String = "") : Person("коротышки"), HasCase{
    fun getState() = state
    fun setState(state: String) {
        this.state = state
    }

    fun getDescription(): String {
        if (state.isEmpty()) return caseDeclension(Case.CREATIVE)
        return "$state ${caseDeclension(Case.CREATIVE)}"
    }

    override fun caseDeclension(toCase: Case): String {
        return when (toCase) {
            Case.NOMINATIVE -> "коротышки"
            Case.ACCUSATIVE -> "коротышек"
            Case.GENITIVE -> "коротышек"
            Case.DATIVE -> "коротышкам"
            Case.CREATIVE -> "коротышками"
            Case.PREPOSITIONAL -> "коротышках"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ShortyGroup) return false
        if (!super.equals(other)) return false

        return state == other.state
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + state.hashCode()
        return result
    }
}