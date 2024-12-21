package dev.lysmux.lab3.story

import dev.lysmux.lab3.story.exception.EmptyStoryException


class Story {
    private val sentences = ArrayList<Sentence>()

    companion object {
        fun build(init: Story.() -> Unit) = Story().apply(init)
    }

    fun sentence(init: SentenceBuilder.() -> Unit) {
        sentences.add(SentenceBuilder().apply(init).build())
    }

    fun asText(): String {
        if (sentences.isEmpty()) throw EmptyStoryException()
        return sentences.joinToString(". ") { it.content }
    }
}
