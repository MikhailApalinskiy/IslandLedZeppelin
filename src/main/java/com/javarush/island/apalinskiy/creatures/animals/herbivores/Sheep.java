package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Sheep extends Herbivore {

    public Sheep() {
        super(70, 15, 3, 140, "\uD83D\uDC11");
    }

    @Override
    protected Animal createOffspring() {
        return new Sheep();
    }
}
