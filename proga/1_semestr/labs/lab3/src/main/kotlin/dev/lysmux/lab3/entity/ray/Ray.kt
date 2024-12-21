package dev.lysmux.lab3.entity.ray

import dev.lysmux.lab3.common.Case
import dev.lysmux.lab3.common.HasCase
import dev.lysmux.lab3.common.Pluralable

class Ray(
    val kind: RayKind,
    val visibility: VisibilityType
) : Pluralable, HasCase{

    constructor(kind: RayKind) : this(kind, VisibilityType.NONE)

    override fun getPluralName() = "лучи"

    override fun toString() = "луч"

    override fun caseDeclension(toCase: Case): String {
        return when(toCase) {
            Case.NOMINATIVE -> "луч"
            Case.GENITIVE -> "луча"
            Case.DATIVE -> "лучу"
            Case.ACCUSATIVE -> "луч"
            Case.CREATIVE -> "лучом"
            Case.PREPOSITIONAL -> "луче"
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Ray) return false

        if (kind != other.kind) return false
        if (visibility != other.visibility) return false

        return true
    }

    override fun hashCode(): Int {
        var result = kind.hashCode()
        result = 31 * result + visibility.hashCode()
        return result
    }
}