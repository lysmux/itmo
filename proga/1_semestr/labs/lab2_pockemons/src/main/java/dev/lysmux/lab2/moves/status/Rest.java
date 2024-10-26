package dev.lysmux.lab2.moves.status;

import ru.ifmo.se.pokemon.*;

/**
 * <b>Описание атаки:</b> Пользователь спит 2 хода, но полностью исцеляется
 */
public final class Rest extends StatusMove {
    public Rest() {
        super(Type.PSYCHIC, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        Effect effect = new Effect();
        effect.turns(2);
        effect.condition(Status.SLEEP);

        pokemon.restore();
        pokemon.setCondition(effect);
    }

    @Override
    protected String describe() {
        return "uses Rest";
    }
}
