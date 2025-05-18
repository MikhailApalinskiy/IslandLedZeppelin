package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import lombok.Getter;

@Getter
public class Mouse extends Herbivore {
    private final double weight = 0.05;
    private final double satietySize = 0.01;
    private final int flockSize = 500;
    private final int speed = 1;

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
