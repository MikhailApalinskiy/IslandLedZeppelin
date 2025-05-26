package com.javarush.island.apalinskiy.map;

import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class Cell {
    private final int x;
    private final int y;

    private final HashSet<Animal> animals = new HashSet<>();
    private final HashSet<AbstractPlant> plants = new HashSet<>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.setCurrentCell(this);
    }

    public void removeAnimal(Animal animal) {
        animals.remove(animal);
        animal.setCurrentCell(null);
    }

    public void addPlant(AbstractPlant plant){
        plants.add(plant);
        plant.setCurrentCell(this);
    }

    public void removePlant(AbstractPlant plant){
        plants.remove(plant);
        plant.setCurrentCell(null);
    }

    public Set<Creature> getCreatures() {
        Set<Creature> all = new HashSet<>();
        all.addAll(animals);
        all.addAll(plants);
        return all;
    }

    public void removeCreature(Creature creature) {
        if (creature instanceof Animal animal) {
            removeAnimal(animal);
        } else if (creature instanceof AbstractPlant plant) {
            removePlant(plant);
        }
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
