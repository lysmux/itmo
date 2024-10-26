package dev.lysmux.lab2.moves.physical;

import ru.ifmo.se.pokemon.PhysicalMove;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

/**
 * <b>Описание атаки:</b> Наносит урон и имеет повышенный коэффициент критического удара (1⁄8 вместо 1⁄24)
 */
public final class DrillRun extends PhysicalMove {
    public DrillRun() {
        super(Type.GROUND, 80, 95);
    }

    @Override
    protected double calcCriticalHit(Pokemon attacker, Pokemon defender) {
        return super.calcCriticalHit(attacker, defender) * 3;
    }

    @Override
    protected String describe() {
        return "uses Drill Run";
    }
}
