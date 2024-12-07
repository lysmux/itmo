package dev.lysmux.lab3.entity.people

import dev.lysmux.lab3.common.CanPut
import dev.lysmux.lab3.common.CanTell
import dev.lysmux.lab3.common.Case
import dev.lysmux.lab3.entity.exception.NameTooShortException
import dev.lysmux.lab3.entity.item.Item
import dev.lysmux.lab3.entity.place.Place
import dev.lysmux.lab3.util.Direction

const val MIN_NAME_LENGTH = 2

open class Person(private val name: String = "он") : CanPut, CanTell {
    init {
        if (name.length < MIN_NAME_LENGTH) throw NameTooShortException(MIN_NAME_LENGTH)
    }

    override fun put(item: Item, place: Place, direction: Direction): String {
        return "положил на ${place.caseDeclension(Case.ACCUSATIVE)} $direction ${item.caseDeclension(Case.NOMINATIVE)}"
    }

    override fun tell() = "принялся рассказывать о том"

    override fun toString() = name
}