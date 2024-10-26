package dev.lysmux.lab2.moves.status;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.StatusMove;
import ru.ifmo.se.pokemon.Type;

/**
 * <b>Описание атаки:</b> Увеличивает атаку пользователя на две ступени
 */
public final class SwordsDance extends StatusMove {
    public SwordsDance() {
        super(Type.NORMAL, 0, 0);
    }

    @Override
    protected void applySelfEffects(Pokemon pokemon) {
        pokemon.setMod(Stat.ATTACK, 2);
    }

    @Override
    protected String describe() {
        return "uses Swords Dance";
    }
}
