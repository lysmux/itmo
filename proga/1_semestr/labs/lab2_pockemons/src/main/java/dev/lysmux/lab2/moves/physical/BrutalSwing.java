package dev.lysmux.lab2.moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Type;


/**
 * <b>Описание атаки:</b> Пользователь яростно размахивает своим телом, нанося урон всему, что находится поблизости
 */
public final class BrutalSwing extends PhysicalMove {
    public BrutalSwing() {
        super(Type.DARK, 60, 100);
    }

    @Override
    protected String describe() {
        return "uses Brutal Swing";
    }
}
