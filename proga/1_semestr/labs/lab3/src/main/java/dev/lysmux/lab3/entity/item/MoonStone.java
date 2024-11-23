package dev.lysmux.lab3.entity.item;

import dev.lysmux.lab3.common.Case;
import dev.lysmux.lab3.entity.ability.GlowsInDark;

import java.util.Objects;

public class MoonStone extends Item {
    @Override
    public String getPluralName() {
        return "лунные камни";
    }

    @Override
    public String caseDeclension(Case to_case) {
        return switch (to_case) {
            case NOMINATIVE, ACCUSATIVE -> "лунный камень";
            case GENITIVE -> "лунного камня";
            case DATIVE -> "лунному камню";
            case CREATIVE -> "лунным камнем";
            case PREPOSITIONAL -> "лунном камне";
        };
    }

    @Override
    public String toString() {
        return "лунный камень";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoonStone moonStone)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(moonStone.toString(), moonStone.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), toString());
    }
}
