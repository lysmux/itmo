package dev.lysmux.lab3.common

import dev.lysmux.lab3.entity.item.Item
import dev.lysmux.lab3.entity.place.Place
import dev.lysmux.lab3.util.Direction

interface CanPut {
    fun put(item: Item, place: Place, direction: Direction): String
}