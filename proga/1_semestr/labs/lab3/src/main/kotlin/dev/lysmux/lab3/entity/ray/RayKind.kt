package dev.lysmux.lab3.entity.ray

enum class RayKind(private val value: String) {
    LIGHT("света"),
    ULTRAVIOLET("ультрафиолетовых"),
    INFRARED("инфракрасных"),
    COSMIC("космических");

    fun getValue() = value
}