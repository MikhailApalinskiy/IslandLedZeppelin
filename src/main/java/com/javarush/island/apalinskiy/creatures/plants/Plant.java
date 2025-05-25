package com.javarush.island.apalinskiy.creatures.plants;

public class Plant extends AbstractPlant {

    public Plant() {
        super(200, 1);
    }

    @Override
    protected AbstractPlant createOffspring() {
        return new Plant();
    }
}
