package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public abstract class Herbivore extends Animal {
    protected Herbivore(double weight, double satietySize, int speed, int flockSize) {
        super(weight, satietySize, speed, flockSize);
    }
}
