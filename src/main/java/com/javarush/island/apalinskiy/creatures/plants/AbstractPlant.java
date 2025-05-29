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

@Getter
@Setter
public abstract class AbstractPlant extends Creature implements Killable, Reproducible {
    private final double weight;
    private final int flockSize;
    private final int serialNumber;
    private final int speed = 1;
    private Cell currentCell;
    private boolean isAlive = true;

    private static int counter = 0;

    public AbstractPlant(int flockSize, double weight) {
        this.serialNumber = ++counter;
        this.flockSize = flockSize;
        this.weight = weight;
    }

    protected abstract AbstractPlant createOffspring();

    @Override
    public void die() {
        this.isAlive = false;
        if (getCurrentCell() != null) {
            getCurrentCell().removeCreature(this);
        }
        setCurrentCell(null);
    }

    @Override
    public void reproduce() {
        Cell cell = getCurrentCell();
        if (cell == null) {
            return;
        }
        cell.getLock().lock();
        try {
            if (!isAlive()) {
                return;
            }
            long sameTypeCount = cell.getPlants().stream()
                    .filter(plant -> plant.getClass() == this.getClass())
                    .count();
            for (AbstractPlant plant : cell.getPlants()) {
                if (plant != this && plant.getClass() == this.getClass()) {
                    if (sameTypeCount >= getFlockSize()) {
                        List<Cell> neighborsInRange = MapUtils.getNeighborsInRange(cell, getSpeed());
                        Collections.shuffle(neighborsInRange);
                        for (Cell neighbor : neighborsInRange) {
                            Cell firstLock = cell.hashCode() < neighbor.hashCode() ? cell : neighbor;
                            Cell secondLock = cell.hashCode() < neighbor.hashCode() ? neighbor : cell;
                            firstLock.getLock().lock();
                            secondLock.getLock().lock();
                            try {
                                long countInNeighbor = neighbor.getPlants().stream()
                                        .filter(p -> p.getClass() == this.getClass())
                                        .count();
                                if (countInNeighbor < getFlockSize()) {
                                    AbstractPlant offSpring = createOffspring();
                                    PlantRegistry.register(offSpring);
                                    neighbor.addPlant(offSpring);
                                    return;
                                }
                            } finally {
                                secondLock.getLock().unlock();
                                firstLock.getLock().unlock();
                            }
                        }
                    } else {
                        AbstractPlant offSpring = createOffspring();
                        PlantRegistry.register(offSpring);
                        cell.addPlant(offSpring);
                    }
                    return;
                }
            }
        } finally {
            cell.getLock().unlock();
        }
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
