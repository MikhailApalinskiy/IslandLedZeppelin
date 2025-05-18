package com.javarush.island.apalinskiy.creatures.animals.herbivores;

public class Boar extends Herbivore {

    public Boar() {
        super(400, 50, 2, 50);
    }

    @Override
    public void eat() {
        while (getCurrentSatiety() <= getSatietySize()){
            //setCurrentSatiety(findAMeal());
        }
    }

    @Override
    public void reproduce() {

    }

    @Override
    public void move() {

    }

    @Override
    public void die() {

    }
}
