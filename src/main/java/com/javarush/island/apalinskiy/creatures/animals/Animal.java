package com.javarush.island.apalinskiy.creatures.animals;

import com.javarush.island.apalinskiy.entity.Killable;
import com.javarush.island.apalinskiy.entity.Moveable;
import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.entity.Eatable;
import com.javarush.island.apalinskiy.entity.Reproducible;
import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.repository.AnimalRegistry;
import com.javarush.island.apalinskiy.util.MapUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Setter
@Getter
public abstract class Animal extends Creature implements Eatable, Moveable, Reproducible, Killable {
    private final double weight;
    private final double satietySize;
    private final int speed;
    private final int flockSize;
    private final int serialNumber;
    private double currentSatiety;
    private boolean isAlive = true;
    private Cell currentCell;

    private static int counter = 0;

    protected Animal(double weight, double satietySize, int speed, int flockSize) {
        this.weight = weight;
        this.satietySize = satietySize;
        this.speed = speed;
        this.flockSize = flockSize;
        this.serialNumber = ++counter;
        this.currentSatiety = 0;
    }

    protected abstract Animal createOffspring();

    @Override
    public void die() {
        this.isAlive = false;
        if (getCurrentCell() != null) {
            getCurrentCell().removeCreature(this);
        }
        setCurrentCell(null);
    }

    @Override
    public void move() {
        Cell cell = getCurrentCell();
        if (cell == null) {
            return;
        }
        cell.getLock().lock();
        try {
            if (!isAlive()) {
                return;
            }
            List<Cell> neighbors = MapUtils.getNeighborsInRange(cell, getSpeed());
            Collections.shuffle(neighbors);
            for (Cell toCell : neighbors) {
                Cell firstLock = cell.hashCode() < toCell.hashCode() ? cell : toCell;
                Cell secondLock = cell.hashCode() < toCell.hashCode() ? toCell : cell;
                firstLock.getLock().lock();
                secondLock.getLock().lock();
                try {
                    long sameTypeCount = toCell.getAnimals().stream()
                            .filter(animal -> animal.getClass() == this.getClass())
                            .count();
                    if (sameTypeCount < getFlockSize()) {
                        cell.removeAnimal(this);
                        toCell.addAnimal(this);
                        setCurrentCell(toCell);
                        return;
                    }
                } finally {
                    secondLock.getLock().unlock();
                    firstLock.getLock().unlock();
                }
            }
        } finally {
            cell.getLock().unlock();
        }
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
            long sameTypeCount = cell.getAnimals().stream()
                    .filter(animal -> animal.getClass() == this.getClass())
                    .count();
            if (sameTypeCount >= getFlockSize()) {
                return;
            }
            for (Creature animal : cell.getAnimals()) {
                if (animal != this && animal.getClass() == this.getClass()) {
                    Animal offspring = createOffspring();
                    AnimalRegistry.register(offspring);
                    cell.addAnimal(offspring);
                    break;
                }
            }
        } finally {
            cell.getLock().unlock();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal other)) return false;
        return serialNumber == other.serialNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serialNumber);
    }
}
