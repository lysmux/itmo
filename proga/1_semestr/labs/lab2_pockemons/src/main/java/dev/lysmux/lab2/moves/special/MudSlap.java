package dev.lysmux.lab2.moves.special;

import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.SpecialMove;
import ru.ifmo.se.pokemon.Stat;
import ru.ifmo.se.pokemon.Type;

/**
 * <b>Описание атаки:</b> Наносит урон и снижает точность цели на одну ступень
 */
public final class MudSlap extends SpecialMove {
    public MudSlap() {
        super(Type.GROUND, 20, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        pokemon.setMod(Stat.ACCURACY, -1);
    }

    @Override
    protected String describe() {
        return "uses MudSlap";
    }
}
