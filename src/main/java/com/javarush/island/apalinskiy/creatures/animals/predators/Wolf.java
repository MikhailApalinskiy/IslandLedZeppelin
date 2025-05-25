package com.javarush.island.apalinskiy.creatures.animals.predators;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Wolf extends Predator {

    public Wolf() {
        super(50, 8, 3, 30);
    }

    @Override
    protected Animal createOffspring() {
        return new Wolf();
    }
}
