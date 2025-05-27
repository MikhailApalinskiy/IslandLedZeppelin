package com.javarush.island.apalinskiy.simulation.worker;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

import java.util.List;

public class SpeciesWorker implements Runnable{
    private final List<Animal> animals;

    public SpeciesWorker(List<Animal> animals) {
        this.animals = animals;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            for (Animal animal : animals) {
                if (animal.isAlive()) {
                    animal.move();
                    animal.eat();
                    animal.reproduce();
                }else {

                }
            }
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
