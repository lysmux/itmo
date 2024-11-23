package dev.lysmux.lab3.entity.exception;

public class NameTooShortException extends RuntimeException {
    final protected int minLength;

    public NameTooShortException(int minLength) {
        super("Имя слишком кроткое");
        this.minLength = minLength;
    }

    @Override
    public String getMessage() {
        return "Имя должно быть не короче " + minLength + " символом";
    }
}
