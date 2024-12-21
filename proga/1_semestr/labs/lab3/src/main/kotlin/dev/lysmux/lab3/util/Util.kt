package dev.lysmux.lab3.util

import java.util.*

fun strCapitalize(str: String): String {
    return str.replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
    }
}