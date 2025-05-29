package com.javarush.island.apalinskiy.entity;

import com.javarush.island.apalinskiy.creatures.Creature;

public interface Reproducible<T extends Creature> {
    T reproduce();
}

