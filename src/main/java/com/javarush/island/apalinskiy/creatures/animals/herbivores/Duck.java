package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import lombok.Getter;

@Getter
public class Duck extends Herbivore {
    private final double weight = 1;
    private final double satietySize = 0.15;
    private final int flockSize = 200;
    private final int speed = 4;

    @Override
    public void eat() {

    }

    @Override
    public void multiple() {

    }

    @Override
    public void move() {

    }
}
