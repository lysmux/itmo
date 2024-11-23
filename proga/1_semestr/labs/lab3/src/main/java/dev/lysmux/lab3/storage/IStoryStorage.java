package dev.lysmux.lab3.storage;

import dev.lysmux.lab3.storage.exception.StoryExistsException;
import dev.lysmux.lab3.storage.exception.StoryNotFoundException;
import dev.lysmux.lab3.story.Story;

import java.util.Map;

public interface IStoryStorage {
    void save(String title, Story story) throws StoryExistsException;
    void delete(String title) throws StoryNotFoundException;
    Story get(String title) throws StoryNotFoundException;
    Map<String, Story> getAll();
}
