package dev.lysmux.lab3.util

class Direction(private val preposition: Preposition, private val content: String) {
    enum class Preposition(val value: String) {
        IN("в"),
        ON("над"),
        UNDER("под"),
        IN_FRONT_OF("перед"),
        BEHIND("позади"),
        BETWEEN("между");
    }

    override fun toString() = "${preposition.value} $content"
}