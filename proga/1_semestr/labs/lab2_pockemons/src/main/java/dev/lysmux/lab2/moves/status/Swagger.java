package dev.lysmux.lab2.moves.status;

import ru.ifmo.se.pokemon.*;

/**
 * <b>Описание атаки:</b> Сбивает цель с толку и повышает ее атаку на две ступени
 */
public final class Swagger extends StatusMove {
    public Swagger() {
        super(Type.NORMAL, 0, 85);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect.confuse(pokemon);
        pokemon.setMod(Stat.ATTACK, +2);
    }

    @Override
    protected String describe() {
        return "uses Swagger";
    }
}
