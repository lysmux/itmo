package dev.lysmux.lab3.story;

import dev.lysmux.lab3.story.exception.EmptyStoryException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Story {
    protected List<Sentence> sentences;

    public Story() {
        sentences = new ArrayList<Sentence>();
    }

    public void addSentences(Sentence... sentences) {
        this.sentences.addAll(List.of(sentences));
    }

    public List<Sentence> getSentences() {
        return sentences;
    }

    public String makeStory() {
        if (sentences.isEmpty()) throw new EmptyStoryException();

        return sentences.stream().map(Sentence::content)
                .collect(Collectors.joining(". "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Story story)) return false;
        return Objects.equals(sentences, story.sentences);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sentences);
    }

    @Override
    public String toString() {
        return "Story{" +
                "sentences=" + sentences +
                '}';
    }
}
