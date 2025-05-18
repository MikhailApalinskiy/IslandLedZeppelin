package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import lombok.Getter;

@Getter
public class Horse extends Herbivore {
    private final double weight = 400;
    private final double satietySize = 60;
    private final int flockSize = 20;
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
