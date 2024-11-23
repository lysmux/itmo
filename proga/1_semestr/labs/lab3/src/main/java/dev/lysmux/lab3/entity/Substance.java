package dev.lysmux.lab3.entity;

import dev.lysmux.lab3.common.Case;
import dev.lysmux.lab3.common.HasCase;
import dev.lysmux.lab3.common.Pluralable;
import dev.lysmux.lab3.entity.ability.Ability;
import dev.lysmux.lab3.entity.place.Place;
import dev.lysmux.lab3.entity.ray.Ray;

import java.util.Objects;

public class Substance implements Pluralable, HasCase {
    protected Ability ability;

    public Substance(Ability ability) {
        this.ability = ability;
    }

    public String acquireAbility() {
        return "приобретают способность " + ability.getDescription();
    }

    public String meet(Place place) {
        return String.join(
                " ",
                "в",
                place.caseDeclension(Case.PREPOSITIONAL),
                "встречаются",
                getPluralName()
        );
    }

    public String influenced(Ray ray) {
        return String.join(
                " ",
                "подвергнутся действию",
                ray.caseDeclension(Case.GENITIVE),
                ray.getKind().getValue()
        );
    }

    @Override
    public String getPluralName() {
        return "вещества";
    }

    @Override
    public String caseDeclension(Case to_case) {
        return switch (to_case) {
            case NOMINATIVE, ACCUSATIVE -> "вещество";
            case GENITIVE -> "вещества";
            case DATIVE -> "веществу";
            case CREATIVE -> "веществом";
            case PREPOSITIONAL -> "веществе";
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Substance substance)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(ability, substance.ability);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ability);
    }

    @Override
    public String toString() {
        return "вещество";
    }
}
