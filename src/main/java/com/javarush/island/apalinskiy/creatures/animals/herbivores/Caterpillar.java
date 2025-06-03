package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Caterpillar extends Herbivore {

    public Caterpillar() {
        super(0.01, 0, 0, 1000, "\uD83D\uDC1B");
    }

    @Override
    protected Animal createOffspring() {
        return new Caterpillar();
    }
}
