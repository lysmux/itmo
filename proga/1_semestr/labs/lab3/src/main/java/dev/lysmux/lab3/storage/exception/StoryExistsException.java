package dev.lysmux.lab3.storage.exception;

public class StoryExistsException extends Exception {
    protected String storyTitle;

    public StoryExistsException(String storyTitle) {
        this.storyTitle = storyTitle;
    }
}
