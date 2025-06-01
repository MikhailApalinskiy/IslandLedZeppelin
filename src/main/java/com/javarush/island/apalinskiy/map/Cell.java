package com.javarush.island.apalinskiy.map;

import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

@Getter
public class Cell {
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
}
