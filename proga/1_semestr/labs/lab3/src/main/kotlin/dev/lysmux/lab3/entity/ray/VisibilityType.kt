package dev.lysmux.lab3.entity.ray

enum class VisibilityType(private val value: String = "") {
    HIDDEN("невидимые"),
    VISIBLE("видимые"),
    NONE();

    fun getValue(): String = value
}