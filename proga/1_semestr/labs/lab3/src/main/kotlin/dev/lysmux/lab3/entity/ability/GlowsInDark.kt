package dev.lysmux.lab3.entity.ability

class GlowsInDark : Ability() {
    override fun getDescription() = toString()

    override fun toString() = "светиться в темноте"

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return other is GlowsInDark
    }

    override fun hashCode() = javaClass.hashCode()
}