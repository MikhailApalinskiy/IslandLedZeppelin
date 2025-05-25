package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Horse extends Herbivore {

    public Horse() {
        super(400, 60, 4, 20);
    }

    @Override
    protected Animal createOffspring() {
        return new Horse();
    }
}
