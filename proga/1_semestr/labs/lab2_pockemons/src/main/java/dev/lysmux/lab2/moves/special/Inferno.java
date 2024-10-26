package dev.lysmux.lab2.moves.special;

import ru.ifmo.se.pokemon.*;

/**
 * <b>Описание атаки:</b> Наносит урон и сжигает цель, если попадает.
 * Сожженные покемоны теряют 1⁄8 своего максимального HP каждый ход, а их атака снижается на 50%
 */
public final class Inferno extends SpecialMove {
    public Inferno() {
        super(Type.FIRE, 100, 50);
    }

    @Override
    protected void applyOppEffects(Pokemon pokemon) {
        if (pokemon.hasType(Type.FIRE)) {
            Effect effect = new Effect();
            effect.turns(-1);
            effect.condition(Status.BURN);
            effect.stat(Stat.HP, (int) pokemon.getStat(Stat.HP) / 8);
            effect.stat(Stat.ATTACK, (int) pokemon.getStat(Stat.ATTACK) / 2);

            pokemon.setCondition(effect);
        }
    }

    @Override
    protected String describe() {
        return "uses Inferno";
    }
}
