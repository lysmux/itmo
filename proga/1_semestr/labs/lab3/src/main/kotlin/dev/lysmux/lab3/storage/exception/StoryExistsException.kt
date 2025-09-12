package dev.lysmux.lab3.storage.exception

class StoryExistsException(private val storyTitle: String) : Exception() {
    override val message: String
        get() = "Story with title `$storyTitle` already exists"
}