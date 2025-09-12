package dev.lysmux.lab3.entity.exception

class NameTooShortException(private val minLength: Int) : RuntimeException("Name too short") {
    override val message: String
        get() = "The name must be at least $minLength character long."
}