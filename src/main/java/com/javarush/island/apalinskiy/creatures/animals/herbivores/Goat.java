package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Goat extends Herbivore {

    public Goat() {
        super(60, 15, 3, 140, "\uD83D\uDC10");
    }

    @Override
    protected Animal createOffspring() {
        return new Goat();
    }
}
