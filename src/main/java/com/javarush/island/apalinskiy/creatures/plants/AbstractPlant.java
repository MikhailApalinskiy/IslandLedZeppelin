package com.javarush.island.apalinskiy.creatures.plants;

import com.javarush.island.apalinskiy.api.entity.Killable;
import com.javarush.island.apalinskiy.api.entity.Reproducible;
import com.javarush.island.apalinskiy.creatures.Creature;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public abstract class AbstractPlant extends Creature implements Killable, Reproducible {
    private final double weight;
    private final int flockSize;
    private final int serialNumber;
    private int currentSatiety;

    private static int counter = 0;

    public AbstractPlant(int flockSize, double weight) {
        this.currentSatiety = 0;
        this.serialNumber = ++counter;
        this.flockSize = flockSize;
        this.weight = weight;
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
