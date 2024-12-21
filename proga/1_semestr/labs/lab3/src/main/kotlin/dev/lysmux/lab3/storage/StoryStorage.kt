package dev.lysmux.lab3.storage

import dev.lysmux.lab3.story.Story

interface StoryStorage {
    fun save(title: String, story: Story)
    fun delete(title: String)
    fun get(title: String): Story
    fun getAll(): Map<String, Story>
    fun clear()
}