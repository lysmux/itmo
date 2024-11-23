package dev.lysmux.lab3.entity.place;

import dev.lysmux.lab3.common.Case;
import dev.lysmux.lab3.entity.item.MoonStone;

import java.util.Objects;

public class Table extends Place {
    @Override
    public String caseDeclension(Case to_case) {
        return switch (to_case) {
            case NOMINATIVE, ACCUSATIVE -> "стол";
            case GENITIVE -> "стола";
            case DATIVE -> "столу";
            case CREATIVE -> "столом";
            case PREPOSITIONAL -> "столе";
        };
    }

    @Override
    public String toString() {
        return "стол";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Table table)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(table.toString(), table.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), toString());
    }
}
