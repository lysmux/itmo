package dev.lysmux.lab3.storage

import dev.lysmux.lab3.storage.exception.StoryExistsException
import dev.lysmux.lab3.storage.exception.StoryNotFoundException
import dev.lysmux.lab3.story.Story

object StoryStorageImpl : StoryStorage {
    private val stories: MutableMap<String, Story> = HashMap()

    override fun save(title: String, story: Story) {
        if (stories.containsKey(title)) throw StoryExistsException(title)
        stories[title] = story
    }

    override fun delete(title: String) {
        if (!stories.containsKey(title)) throw StoryNotFoundException(title)
        stories.remove(title)
    }

    override fun get(title: String): Story {
        if (!stories.containsKey(title)) throw StoryNotFoundException(title)
        return stories[title]!!
    }

    override fun getAll(): Map<String, Story> = stories

    override fun clear() = stories.clear()

}