package com.javarush.island.apalinskiy.creatures.animals;

import com.javarush.island.apalinskiy.api.entity.Killable;
import com.javarush.island.apalinskiy.api.entity.Moveable;
import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.api.entity.Eatable;
import com.javarush.island.apalinskiy.api.entity.Reproducible;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
public abstract class Animal extends Creature implements Eatable, Moveable, Reproducible, Killable {
    private final double weight;
    private final double satietySize;
    private final int speed;
    private final int flockSize;
    private final int serialNumber;
    private int currentSatiety;

    private static int counter = 0;

    protected Animal(double weight, double satietySize, int speed, int flockSize) {
        this.weight = weight;
        this.satietySize = satietySize;
        this.speed = speed;
        this.flockSize = flockSize;
        this.serialNumber = ++counter;
        this.currentSatiety = 0;
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
