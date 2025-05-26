package com.javarush.island.apalinskiy.creatures.animals;

import com.javarush.island.apalinskiy.entity.Killable;
import com.javarush.island.apalinskiy.entity.Moveable;
import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.entity.Eatable;
import com.javarush.island.apalinskiy.entity.Reproducible;
import com.javarush.island.apalinskiy.map.Cell;
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
        if (!isAlive() || getCurrentCell() == null) {
            return;
        }
        List<Cell> neighborsInRange = MapUtils.getNeighborsInRange(getCurrentCell(), getSpeed());
        Collections.shuffle(neighborsInRange);
        for (Cell neighbor : neighborsInRange) {
            long countInNeighbor = neighbor.getAnimals().stream()
                    .filter(animal -> animal.getClass() == this.getClass())
                    .count();
            if (countInNeighbor < getFlockSize()) {
                getCurrentCell().removeAnimal(this);
                neighbor.addAnimal(this);
                return;
            }
        }
    }

    @Override
    public void reproduce() {
        if (!isAlive() || getCurrentCell() == null) {
            return;
        }
        long sameTypeCount = getCurrentCell().getAnimals().stream()
                .filter(animal -> animal.getClass() == this.getClass())
                .count();
        if (sameTypeCount >= getFlockSize()) {
            return;
        }
        for (Creature creature : getCurrentCell().getCreatures()) {
            if (creature != this && creature.getClass() == this.getClass()) {
                getCurrentCell().addAnimal(createOffspring());
                break;
            }
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
