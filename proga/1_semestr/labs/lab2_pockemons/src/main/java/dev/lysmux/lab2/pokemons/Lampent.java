package dev.lysmux.lab2.pokemons;

import dev.lysmux.lab2.moves.special.Inferno;

public class Lampent extends Litwick {
    public Lampent(String name, int level) {
        super(name, level);

        setStats(60, 40, 60, 95, 60, 55);
        addMove(new Inferno());
    }
}
