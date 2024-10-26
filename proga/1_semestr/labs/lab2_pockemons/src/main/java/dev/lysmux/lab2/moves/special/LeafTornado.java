package dev.lysmux.lab2.moves.special;

import ru.ifmo.se.pokemon.*;

/**
 * <b>Описание атаки:</b> Наносит урон и с вероятностью 30% снижает точность цели на одну ступень
 */
public final class LeafTornado extends SpecialMove {
    public LeafTornado() {
        super(Type.GRASS, 65, 90);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        Effect effect = new Effect();
        effect.chance(0.3);
        effect.stat(Stat.ACCURACY, -1);

        pokemon.addEffect(effect);
    }

    @Override
    protected String describe() {
        return "uses Leaf Tornado";
    }
}
