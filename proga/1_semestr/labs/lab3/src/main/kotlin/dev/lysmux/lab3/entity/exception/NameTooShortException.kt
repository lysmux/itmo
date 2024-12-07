package dev.lysmux.lab3.entity.exception

class NameTooShortException(private val minLength: Int) : RuntimeException("Имя слишком кроткое"){
    override val message: String
        get() = "Имя должно быть не короче $minLength символов"
}