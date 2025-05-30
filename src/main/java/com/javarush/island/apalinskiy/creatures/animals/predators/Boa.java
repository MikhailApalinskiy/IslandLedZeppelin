package com.javarush.island.apalinskiy.creatures.animals.predators;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Boa extends Predator {

    public Boa() {
        super(15, 3, 1, 30);
    }

    @Override
    protected Animal createOffspring() {
        return new Boa();
    }
}
