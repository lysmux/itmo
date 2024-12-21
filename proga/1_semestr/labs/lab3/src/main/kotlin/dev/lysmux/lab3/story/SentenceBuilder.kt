package dev.lysmux.lab3.story

class SentenceBuilder {
    private var content = StringBuilder()

    fun content(vararg parts: String): String {
        val content = parts.joinToString(" ")
        if (this.content.isEmpty()) this.content.append(content)

        return content
    }

    infix fun String.and(other: String) = combine(other, "и")

    infix fun String.that(other: String) = combine(other, "что")

    infix fun String.which(other: String) = combine(other, "которые")

    infix fun String.after(other: String) = combine(other, "после того как")

    private fun combine(other: String, connector: String? = null): String {
        this.content.append(", ")
        if (connector != null) this.content.append("$connector ")
        this.content.append(other)

        return this.content.toString()
    }

    fun build() = Sentence(content.toString())
}
