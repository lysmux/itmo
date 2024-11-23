package dev.lysmux.lab3.storage.exception;

public class StoryNotFoundException extends Exception {
    final protected String storyTitle;

    public StoryNotFoundException(String storyTitle) {
        this.storyTitle = storyTitle;
    }
}
