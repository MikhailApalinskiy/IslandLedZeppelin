package com.javarush.island.apalinskiy.simulation.worker;

import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import com.javarush.island.apalinskiy.map.Cell;

import java.util.ArrayList;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadLocalRandom;

public class CellWorker implements Runnable {
    private final Cell cell;
    private final Phaser phaser;

    public CellWorker(Cell cell, Phaser phaser) {
        this.cell = cell;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        try {
            ArrayList<Animal> animals = cell.getAnimals();
            ArrayList<AbstractPlant> plants = cell.getPlants();
            while (!Thread.currentThread().isInterrupted()) {
                for (Animal animal : animals) {
                    if (!animal.isAlive()) {
                        continue;
                    }
                    animal.eat();
                    if (animal.getCurrentSatiety() == 0) {
                        animal.die();
                    }
                    int animalReproduceChance = 30;
                    if (ThreadLocalRandom.current().nextInt(100) < animalReproduceChance) {
                        Animal offspring = animal.reproduce();
                        if (offspring != null) {
                            cell.addNewAnimal(offspring);
                        }
                    }
                    animal.move();
                }
                for (AbstractPlant plant : plants) {
                    if (!plant.isAlive()) {
                        continue;
                    }
                    int plantReproduceChance = 30;
                    if (ThreadLocalRandom.current().nextInt(100) < plantReproduceChance) {
                        AbstractPlant offSpring = plant.reproduce();
                        if (offSpring != null && offSpring.getCurrentCell() == cell) {
                            cell.addNewPlant(offSpring);
                        }
                    }
                }
                phaser.arriveAndAwaitAdvance();
                animals.removeIf(animal -> !animal.isAlive() || animal.getCurrentCell() != cell);
                animals.addAll(cell.getNewAnimals());
                cell.getNewAnimals().clear();
                plants.removeIf(plant -> !plant.isAlive());
                plants.addAll(cell.getNewPlants());
                cell.getNewPlants().clear();
                phaser.arriveAndAwaitAdvance();
            }
        } finally {
            phaser.arriveAndDeregister();
        }
    }
}
