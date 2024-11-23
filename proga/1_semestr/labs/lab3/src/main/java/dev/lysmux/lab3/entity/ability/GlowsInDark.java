package dev.lysmux.lab3.entity.ability;

import java.util.Objects;

public class GlowsInDark extends Ability {
    @Override
    public String getDescription() {
        return toString();
    }

    @Override
    public String toString() {
        return "светиться в темноте";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GlowsInDark glowsInDark)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(glowsInDark.toString(), glowsInDark.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), toString());
    }
}
