package com.javarush.island.apalinskiy.creatures.plants;

import com.javarush.island.apalinskiy.api.entity.Killable;
import com.javarush.island.apalinskiy.api.entity.Reproducible;
import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.map.Cell;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public abstract class AbstractPlant extends Creature implements Killable, Reproducible {
    private final double weight;
    private final int flockSize;
    private final int serialNumber;
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
        if (!isAlive()){
            return;
        }
        if (getCurrentCell() == null) {
            return;
        }
        long sameTypeCount = getCurrentCell().getPlants().stream()
                .filter(plant -> plant.getClass() == this.getClass())
                .count();
        if (sameTypeCount >= getFlockSize()) {
            return;
        }
        for (Creature creature : getCurrentCell().getCreatures()) {
            if (creature != this && creature.getClass() == this.getClass()) {
                getCurrentCell().addPlant(createOffspring());
                break;
            }
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
