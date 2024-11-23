package dev.lysmux.lab3.entity.place;

import dev.lysmux.lab3.common.Case;
import dev.lysmux.lab3.entity.item.MoonStone;

import java.util.Objects;

public class Nature extends Place {
    @Override
    public String caseDeclension(Case to_case) {
        return switch (to_case) {
            case NOMINATIVE -> "природа";
            case GENITIVE -> "природы";
            case DATIVE, PREPOSITIONAL -> "природе";
            case ACCUSATIVE -> "природу";
            case CREATIVE -> "природой";
        };
    }

    @Override
    public String toString() {
        return "природа";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nature nature)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(nature.toString(), nature.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), toString());
    }
}
