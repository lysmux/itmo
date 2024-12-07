package dev.lysmux.lab3.storage

import dev.lysmux.lab3.storage.exception.StoryExistsException
import dev.lysmux.lab3.storage.exception.StoryNotFoundException
import dev.lysmux.lab3.story.Story

interface IStoryStorage {
    @Throws(StoryExistsException::class)
    fun save(title: String, story: Story)

    @Throws(StoryNotFoundException::class)
    fun delete(title: String)

    @Throws(StoryNotFoundException::class)
    fun get(title: String): Story

    fun getAll(): Map<String, Story>
}