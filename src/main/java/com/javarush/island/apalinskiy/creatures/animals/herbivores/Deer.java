package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public class Deer extends Herbivore {

    public Deer() {
        super(300, 50, 4, 20);
    }

    @Override
    protected Animal createOffspring() {
        return new Deer();
    }
}
