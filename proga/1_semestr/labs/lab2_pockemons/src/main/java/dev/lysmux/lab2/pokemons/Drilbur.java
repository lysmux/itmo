package dev.lysmux.lab2.pokemons;

import dev.lysmux.lab2.moves.physical.DrillRun;
import dev.lysmux.lab2.moves.special.MudSlap;
import dev.lysmux.lab2.moves.status.SwordsDance;
import ru.ifmo.se.pokemon.Pokemon;
import ru.ifmo.se.pokemon.Type;

public class Drilbur extends Pokemon {
    public Drilbur(String name, int level) {
        super(name, level);

        setType(Type.GROUND);
        setStats(60, 85, 40, 30, 45, 68);
        setMove(
                new DrillRun(),
                new MudSlap(),
                new SwordsDance()
        );
    }
}
