package dev.lysmux.lab3.util

class Direction (private val preposition: Preposition, private val content: String){
    enum class Preposition(private val value: String) {
        IN("в"),
        ON("над"),
        UNDER("под"),
        IN_FRONT_OF("перед"),
        BEHIND("позади"),
        BETWEEN("между");

        fun getValue() = value
    }

    override fun toString() = "${preposition.getValue()} $content"
}