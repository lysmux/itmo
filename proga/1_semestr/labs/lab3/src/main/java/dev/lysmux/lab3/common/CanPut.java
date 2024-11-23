package dev.lysmux.lab3.common;

import dev.lysmux.lab3.util.Direction;
import dev.lysmux.lab3.entity.item.Item;
import dev.lysmux.lab3.entity.place.Place;

public interface CanPut {
    String put(Item item, Place place, Direction direction);
}
