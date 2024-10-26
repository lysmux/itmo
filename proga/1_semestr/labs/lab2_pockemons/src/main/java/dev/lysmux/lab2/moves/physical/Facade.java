package dev.lysmux.lab2.moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Status;
import ru.ifmo.se.pokemon.Type;

/**
 * <b>Описание атаки:</b> Наносит урон и бьет с удвоенной силой (140),
 * если пользователь обожжен, отравлен или парализован
 */
public final class Facade extends PhysicalMove {
    public Facade() {
        super(Type.NORMAL, 70, 100);
    }

    @Override
    protected double calcBaseDamage(Pokemon pokemon, Pokemon pokemon1) {
        double damage = super.calcBaseDamage(pokemon, pokemon1);

        return switch (pokemon.getCondition()) {
            case BURN, POISON, PARALYZE -> damage * 2;
            default -> damage;
        };

    }

    @Override
    protected String describe() {
        return "uses Facade";
    }
}
