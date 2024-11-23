package dev.lysmux.lab3.entity.people;

import dev.lysmux.lab3.common.Case;
import dev.lysmux.lab3.common.HasCase;

import java.util.Objects;

public class ShortyGroup extends Person implements HasCase {
    protected String state;

    public ShortyGroup(String state) {
        super("коротышки");
        this.state = state;
    }

    public ShortyGroup() {
        this("");
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDescription() {
        if (state.isEmpty()) return caseDeclension(Case.CREATIVE);
        return state + " " + caseDeclension(Case.CREATIVE);
    }

    @Override
    public String caseDeclension(Case to_case) {
        return switch (to_case) {
            case NOMINATIVE -> "коротышки";
            case GENITIVE, ACCUSATIVE -> "коротышек";
            case DATIVE -> "коротышкам";
            case CREATIVE -> "коротышками";
            case PREPOSITIONAL -> "коротышках";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ShortyGroup that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(state, that.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), state);
    }
}
