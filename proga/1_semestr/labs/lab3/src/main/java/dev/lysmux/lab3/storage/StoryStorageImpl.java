package dev.lysmux.lab3.storage;

import dev.lysmux.lab3.storage.exception.StoryExistsException;
import dev.lysmux.lab3.storage.exception.StoryNotFoundException;
import dev.lysmux.lab3.story.Story;

import java.util.HashMap;
import java.util.Map;

public class StoryStorageImpl implements IStoryStorage{
    private static StoryStorageImpl INSTANCE;

    protected Map<String, Story> stories;

    private StoryStorageImpl() {
        stories = new HashMap<>();
    }

    public static StoryStorageImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new StoryStorageImpl();
        }
        return INSTANCE;
    }

    @Override
    public void save(String title, Story story) throws StoryExistsException {
        if (stories.containsKey(title)) throw new StoryExistsException(title);
        stories.put(title, story);
    }

    @Override
    public void delete(String title) throws StoryNotFoundException {
        if (!stories.containsKey(title)) throw new StoryNotFoundException(title);
        stories.remove(title);
    }

    @Override
    public Story get(String title) throws StoryNotFoundException {
        if (!stories.containsKey(title)) throw new StoryNotFoundException(title);
        return stories.get(title);
    }

    @Override
    public Map<String, Story> getAll() {
        return stories;
    }
}
