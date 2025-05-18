package com.javarush.island.apalinskiy.creatures.plants;

import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.api.entity.Multiplying;
import lombok.Getter;

@Getter
public class Plant extends Creature implements Multiplying {
    private final double weight = 1;
    private final int flockSize = 200;

    @Override
    public void multiple() {

    }
}
