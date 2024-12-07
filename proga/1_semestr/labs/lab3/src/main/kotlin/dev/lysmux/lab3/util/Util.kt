package dev.lysmux.lab3.util

import java.util.*

object Util {
    fun strCapitalize(str: String) = str.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
}