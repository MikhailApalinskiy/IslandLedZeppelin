package com.javarush.island.apalinskiy.creatures.animals.predators;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Fox extends Predator {

    public Fox() {
        super(8, 2, 2, 30);
    }

    @Override
    protected Animal createOffspring() {
        return new Fox();
    }
}
