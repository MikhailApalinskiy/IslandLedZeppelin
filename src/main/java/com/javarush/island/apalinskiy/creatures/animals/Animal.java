package com.javarush.island.apalinskiy.creatures.animals;

import com.javarush.island.apalinskiy.api.entity.Moveable;
import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.api.entity.Eatable;
import com.javarush.island.apalinskiy.api.entity.Multiplying;

public abstract class Animal extends Creature implements Eatable, Moveable, Multiplying {
}
