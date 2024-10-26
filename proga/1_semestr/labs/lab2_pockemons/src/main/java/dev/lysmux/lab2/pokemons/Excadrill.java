package dev.lysmux.lab2.pokemons;

import dev.lysmux.lab2.moves.physical.BrutalSwing;
import ru.ifmo.se.pokemon.Type;

public final class Excadrill extends Drilbur {
    public Excadrill(String name, int level) {
        super(name, level);

        addType(Type.STEEL);
        setStats(110, 135, 60, 50, 65, 88);
        addMove(new BrutalSwing());
    }
}
