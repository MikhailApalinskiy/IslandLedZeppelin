package com.javarush.island.apalinskiy.creatures.animals.predators;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Bear extends Predator {

    public Bear() {
        super(500, 80, 2, 5);
    }

    @Override
    protected Animal createOffspring() {
        return new Bear();
    }
}
