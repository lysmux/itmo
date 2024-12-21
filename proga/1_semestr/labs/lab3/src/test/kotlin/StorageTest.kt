import dev.lysmux.lab3.storage.StoryStorageImpl
import dev.lysmux.lab3.storage.exception.StoryExistsException
import dev.lysmux.lab3.storage.exception.StoryNotFoundException
import dev.lysmux.lab3.story.Story
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.test.BeforeTest
import kotlin.test.assertEquals


class StorageTest {
    private val storage = StoryStorageImpl

    private fun generateStory() = Story.build {
        sentence { content("test") }
    }

    @BeforeTest
    fun setUp() = storage.clear()

    @Test
    fun testSaveSuccess() {
        val storyTitle = "Test title"
        val story = generateStory()

        storage.save(title = storyTitle, story = story)
        val storyFromStorage = storage.get(title = storyTitle)

        assertEquals(story, storyFromStorage)
    }

    @Test
    fun testSaveDuplicate() {
        val storyTitle = "Test title"
        val story = generateStory()

        storage.save(title = storyTitle, story = story)
        assertThrows<StoryExistsException> { storage.save(title = storyTitle, story = story) }
    }

    @Test
    fun testGetStoryNotExists() {
        assertThrows<StoryNotFoundException> { storage.get("Any story") }
    }

    @Test
    fun testGetAllStories() {
        val stories = mapOf(
            "Story #1" to generateStory(),
            "Story #2" to generateStory()
        )
        stories.forEach { (title, story) -> storage.save(title = title, story = story) }

        assertEquals(stories, storage.getAll())
    }

    @Test
    fun testDelete() {
        val storyTitle = "Test title"
        val story = generateStory()

        storage.save(title = storyTitle, story = story)
        storage.delete(title = storyTitle)

        assertThrows<StoryNotFoundException> { storage.get(title = storyTitle) }
    }
}