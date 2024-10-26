package dev.lysmux.lab2.pokemons;

import dev.lysmux.lab2.moves.physical.Facade;
import dev.lysmux.lab2.moves.special.LeafTornado;
import dev.lysmux.lab2.moves.status.Growth;
import dev.lysmux.lab2.moves.status.Swagger;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public final class Carnivine extends Pokemon {
    public Carnivine(String name, int level) {
        super(name, level);

        setType(Type.GRASS);
        setStats(74, 100, 72, 90, 72, 46);
        setMove(
                new Swagger(),
                new Growth(),
                new LeafTornado(),
                new Facade()
        );
    }
}
