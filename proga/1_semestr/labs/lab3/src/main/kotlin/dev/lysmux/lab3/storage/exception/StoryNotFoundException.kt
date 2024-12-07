package dev.lysmux.lab3.storage.exception

class StoryNotFoundException(private val storyTitle: String) : Exception()  {
    override val message: String
        get() = "Story with title `$storyTitle` not found"
}