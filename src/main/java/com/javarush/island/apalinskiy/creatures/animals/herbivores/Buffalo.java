package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Buffalo extends Herbivore {

    public Buffalo() {
        super(700, 100, 3, 10);
    }

    @Override
    protected Animal createOffspring() {
        return new Buffalo();
    }
}
