package dev.lysmux.lab2;

import dev.lysmux.lab2.pokemons.*;
import ru.ifmo.se.pokemon.Battle;

public class Main {
    public static void main(String[] args) {
        Battle battle = new Battle();

        battle.addAlly(new Carnivine("Stormdrake", 72));
        battle.addAlly(new Chandelure("Abysspawn", 88));
        battle.addAlly(new Drilbur("Pyroscorch", 91));

        battle.addFoe(new Excadrill("Frostbloom", 75));
        battle.addFoe(new Lampent("Voltiger", 82));
        battle.addFoe(new Litwick("Dreadtalon", 95));

        battle.go();
    }
}
