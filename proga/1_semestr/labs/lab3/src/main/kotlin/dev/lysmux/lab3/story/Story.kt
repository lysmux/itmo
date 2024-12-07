package dev.lysmux.lab3.story

import dev.lysmux.lab3.story.exception.EmptyStoryException

class Story(private val sentences: MutableList<Sentence> = ArrayList()) {
    fun addSentences(vararg sentences: Sentence) = this.sentences.addAll(sentences)

    fun getSentences() = sentences

    fun makeStory(): String {
        if (sentences.isEmpty()) throw EmptyStoryException()

        return sentences.joinToString(" ") { it.content }
    }

    override fun equals(other: Any?): Boolean {
        return other is Story && sentences == other.sentences
    }

    override fun hashCode(): Int {
        return sentences.hashCode()
    }
}
