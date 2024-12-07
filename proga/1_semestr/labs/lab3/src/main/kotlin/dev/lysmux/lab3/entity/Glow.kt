package dev.lysmux.lab3.entity

import dev.lysmux.lab3.common.HasAlternativeName

class Glow : HasAlternativeName {
    override fun getAlternativeName() = "люминесценция"

    override fun toString() = "свечение"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return true
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }
}