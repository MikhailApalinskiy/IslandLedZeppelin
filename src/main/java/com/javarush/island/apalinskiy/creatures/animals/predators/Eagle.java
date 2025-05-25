package com.javarush.island.apalinskiy.creatures.animals.predators;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Eagle extends Predator {

    public Eagle() {
        super(6, 1, 3, 20);
    }

    @Override
    protected Animal createOffspring() {
        return new Eagle();
    }
}
