package dev.lysmux.lab3.entity.people;

import dev.lysmux.lab3.common.CanPut;
import dev.lysmux.lab3.common.CanTell;
import dev.lysmux.lab3.common.Case;
import dev.lysmux.lab3.entity.exception.NameTooShortException;
import dev.lysmux.lab3.entity.item.Item;
import dev.lysmux.lab3.entity.place.Place;
import dev.lysmux.lab3.util.Direction;

import java.util.Objects;

public class Person implements CanPut, CanTell {
    public final static int MIN_NAME_LENGTH = 2;

    private final String name;

    public Person() {
        this("он");
    }

    public Person(String name) {
        if (name.length() < MIN_NAME_LENGTH) throw new NameTooShortException(MIN_NAME_LENGTH);
        this.name = name;
    }

    @Override
    public String put(Item item, Place place, Direction direction) {
        return String.join(" ",
                "положил на",
                place.caseDeclension(Case.ACCUSATIVE),
                direction.toString(),
                item.caseDeclension(Case.NOMINATIVE)
        );

    }

    @Override
    public String tell() {
        return "принялся рассказывать о том";
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person person)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(person.toString(), person.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), toString());
    }
}
