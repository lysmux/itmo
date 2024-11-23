package dev.lysmux.lab3.story;

public class SentenceFactory {
    public static Sentence createSentence(String... parts) {
        String content = String.join(" ", parts);
        return new Sentence(content);
    }
}
