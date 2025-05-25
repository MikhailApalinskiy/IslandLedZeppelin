package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Rabbit extends Herbivore {

    public Rabbit() {
        super(2, 0.45, 2, 150);
    }

    @Override
    protected Animal createOffspring() {
        return new Rabbit();
    }
}
