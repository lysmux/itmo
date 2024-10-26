package dev.lysmux.lab2.pokemons;

import dev.lysmux.lab2.moves.special.Psychic;
import dev.lysmux.lab2.moves.status.Rest;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Litwick extends Pokemon {
    public Litwick(String name, int level) {
        super(name, level);

        setType(Type.GHOST, Type.FIRE);
        setStats(50, 30, 55, 65, 55, 20);
        setMove(
                new Rest(),
                new Psychic()
        );
    }
}
