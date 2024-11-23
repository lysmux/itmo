package dev.lysmux.lab3.entity;

import dev.lysmux.lab3.common.HasAlternativeName;
import dev.lysmux.lab3.entity.item.MoonStone;

import java.util.Objects;

public class Glow implements HasAlternativeName {
    @Override
    public String getAlternativeName() {
        return "люминесценция";
    }

    @Override
    public String toString() {
        return "свечение";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Glow glow)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(glow.toString(), glow.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), toString());
    }
}
