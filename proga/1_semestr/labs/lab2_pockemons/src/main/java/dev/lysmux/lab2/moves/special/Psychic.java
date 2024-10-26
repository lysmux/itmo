package dev.lysmux.lab2.moves.special;

import ru.ifmo.se.pokemon.*;

/**
 * <b>Описание атаки:</b> Наносит урон и имеет 10%-ный шанс снизить Специальную защиту цели на одну ступень
 */
public final class Psychic extends SpecialMove {
    public Psychic() {
        super(Type.PSYCHIC, 90, 100);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect effect = new Effect();
        effect.stat(Stat.SPECIAL_DEFENSE, -1);
        effect.chance(0.1);
        effect.turns(-1);

        pokemon.addEffect(effect);
    }

    @Override
    protected String describe() {
        return "uses Psychic";
    }
}
