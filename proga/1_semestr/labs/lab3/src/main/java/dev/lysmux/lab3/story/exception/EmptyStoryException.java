package dev.lysmux.lab3.story.exception;

public class EmptyStoryException extends IllegalStateException {
    public EmptyStoryException() {
        super("Пустой рассказ");
    }

    @Override
    public String getMessage() {
        return "В рассказ не добавлено содержимое";
    }
}
