package com.javarush.island.apalinskiy.map;

import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public class Cell {
    private final int plantLifeCycle = 6;
    private final int x;
    private final int y;
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final ArrayList<AbstractPlant> plants = new ArrayList<>();
    private final Queue<Animal> newAnimals = new ConcurrentLinkedQueue<>();
    private final Queue<AbstractPlant> newPlants = new ConcurrentLinkedQueue<>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addNewAnimal(Animal animal) {
        newAnimals.add(animal);
        animal.setCurrentCell(this);
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.setCurrentCell(this);
    }

    public void addNewPlant(AbstractPlant plant) {
        newPlants.add(plant);
        plant.setCurrentCell(this);
    }

    public void addPlant(AbstractPlant plant) {
        plants.add(plant);
        plant.setCurrentCell(this);
    }

    public Set<Creature> getCreatures() {
        Set<Creature> all = new HashSet<>();
        all.addAll(animals);
        all.addAll(plants);
        return all;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cell cell)) return false;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    public void processLogicStep() {
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
                    addNewAnimal(offspring);
                }
            }
            animal.move();
        }
        for (AbstractPlant plant : plants) {
            if (!plant.isAlive()) {
                continue;
            }
            int i = plant.getTickCounter();
            i++;
            plant.setTickCounter(i);
            int plantReproduceChance = 20;
            if (ThreadLocalRandom.current().nextInt(100) < plantReproduceChance) {
                AbstractPlant offSpring = plant.reproduce();
                if (offSpring != null && offSpring.getCurrentCell() == this) {
                    addNewPlant(offSpring);
                }
            }
            if (i >= plantLifeCycle) {
                plant.die();
            }
        }
    }

    public void processCleanupStep() {
        animals.removeIf(a -> !a.isAlive() || a.getCurrentCell() != this);
        animals.addAll(newAnimals);
        newAnimals.clear();
        plants.removeIf(p -> !p.isAlive());
        plants.addAll(newPlants);
        newPlants.clear();
    }
}
