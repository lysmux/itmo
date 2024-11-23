package dev.lysmux.lab3.util;

public class Direction {
    public enum Preposition {
        IN("в"),
        ON("над"),
        UNDER("под"),
        IN_FRONT_OF("перед"),
        BEHIND("позади"),
        BETWEEN("между");

        private final String value;

        Preposition(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    protected final String content;
    protected final Preposition preposition;

    public Direction(Preposition preposition, String content) {
        this.content = content;
        this.preposition = preposition;
    }

    @Override
    public String toString() {
        return preposition.getValue() + " " + content;
    }
}
