package com.javarush.island.apalinskiy.creatures.animals.predators;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

public abstract class Predator extends Animal {
    protected Predator(double weight, double satietySize, int speed, int flockSize) {
        super(weight, satietySize, speed, flockSize);
    }
}
