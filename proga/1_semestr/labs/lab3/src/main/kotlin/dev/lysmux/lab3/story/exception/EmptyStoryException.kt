package dev.lysmux.lab3.story.exception

class EmptyStoryException : IllegalStateException("Empty story"){
    override val message: String
        get() = "Story does not contains any content"
}