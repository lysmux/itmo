package dev.lysmux.lab3.story;

public record Sentence(String content) {
    public Sentence and(String text) {
        return new Sentence(content.concat(", и ").concat(text));
    }

    public Sentence but(String text) {
        return new Sentence(content.concat(", но ").concat(text));
    }

    public Sentence that(String text) {
        return new Sentence(content.concat(", что ").concat(text));
    }

    public Sentence which(String text) {
        return new Sentence(content.concat(", которые ").concat(text));
    }

    public Sentence after(String text) {
        return new Sentence(content.concat(", после того как ").concat(text));
    }

    public Sentence comma(String text) {
        return new Sentence(content.concat(", ").concat(text));
    }
}
