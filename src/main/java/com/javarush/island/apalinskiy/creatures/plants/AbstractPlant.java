package com.javarush.island.apalinskiy.creatures.plants;

import com.javarush.island.apalinskiy.entity.Killable;
import com.javarush.island.apalinskiy.entity.Reproducible;
import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.repository.PlantRegistry;
import com.javarush.island.apalinskiy.util.MapUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base class for all plant-like entities in the simulation.
 * <p>
 * Implements {@link Killable} and {@link Reproducible} to support lifecycle management
 * and propagation of plant species within a cell and its neighbors.
 * <p>
 * This entity overrides {@code equals()} and {@code hashCode()} using a unique serial number.
 * These overrides were originally introduced to ensure correct behavior in hash-based collections
 * (such as {@link java.util.HashSet}) used in earlier versions of the simulation.
 * <p>
 * After redesigning the simulation into a step-by-step model where threads process
 * map regions instead of species and synchronized collections are used instead of sets,
 * the requirement for identity-based hashing was removed. However, the overrides remain
 * for potential future use or collection logic that may again rely on object identity.
 * <p>
 * The class also maintains an {@code emoji} field used for UI representation.
 */
@Getter
@Setter
public abstract class AbstractPlant extends Creature implements Killable, Reproducible<AbstractPlant> {
    private final double weight;
    private final int flockSize;
    private final int serialNumber;
    private final int speed = 1;
    private volatile boolean isAlive = true;
    private Cell currentCell;
    private final String emoji;
    private int tickCounter = 0;

    private static int counter = 0;

    public AbstractPlant(int flockSize, double weight, String emoji) {
        this.serialNumber = ++counter;
        this.flockSize = flockSize;
        this.weight = weight;
        this.emoji = emoji;
    }

    protected abstract AbstractPlant createOffspring();

    @Override
    public void die() {
        isAlive = false;
        setCurrentCell(null);
        PlantRegistry.unregister(this);
    }

    @Override
    public AbstractPlant reproduce() {
        Cell cell = getCurrentCell();
        if (cell == null) {
            return null;
        }
        int sameTypeCount = 0;
        for (AbstractPlant a : cell.getPlants()) {
            if (a.getClass() == this.getClass()) sameTypeCount++;
        }
        for (AbstractPlant a : cell.getNewPlants()) {
            if (a.getClass() == this.getClass()) sameTypeCount++;
        }
        for (AbstractPlant plant : cell.getPlants()) {
            if (plant != this && plant.getClass() == this.getClass()) {
                if (sameTypeCount >= getFlockSize()) {
                    List<Cell> neighbors = MapUtils.getNeighborsInRange(cell, getSpeed());
                    Collections.shuffle(neighbors);
                    for (Cell neighbor : neighbors) {
                        int neighborCount = 0;
                        for (AbstractPlant a : neighbor.getPlants()) {
                            if (a.getClass() == this.getClass()) neighborCount++;
                        }
                        for (AbstractPlant a : neighbor.getNewPlants()) {
                            if (a.getClass() == this.getClass()) neighborCount++;
                        }
                        if (neighborCount < getFlockSize()) {
                            AbstractPlant offspring = createOffspring();
                            PlantRegistry.register(offspring);
                            neighbor.addNewPlant(offspring);
                            return offspring;
                        }
                    }
                } else {
                    AbstractPlant offspring = createOffspring();
                    PlantRegistry.register(offspring);
                    cell.addNewPlant(offspring);
                    return offspring;
                }
                return null;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractPlant that)) return false;
        return serialNumber == that.serialNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serialNumber);
    }
}
