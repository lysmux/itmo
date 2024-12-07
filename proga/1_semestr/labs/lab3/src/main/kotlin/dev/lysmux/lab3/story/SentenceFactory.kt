package dev.lysmux.lab3.story

class SentenceFactory {
    companion object {
        fun createSentence(vararg parts: String): Sentence {
            return Sentence(parts.joinToString(" "))
        }
    }
}