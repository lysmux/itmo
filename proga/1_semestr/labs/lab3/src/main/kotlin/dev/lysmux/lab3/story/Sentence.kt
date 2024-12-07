package dev.lysmux.lab3.story

data class Sentence(val content: String) {
    fun and(text: String) = Sentence("$content, и $text")
    fun but(text: String) = Sentence("$content, но $text")
    fun that(text: String) = Sentence("$content, что $text")
    fun which(text: String) = Sentence("$content, которые $text")
    fun after(text: String) = Sentence("$content, после того как $text")
    fun comma(text: String) = Sentence("$content, $text")
}