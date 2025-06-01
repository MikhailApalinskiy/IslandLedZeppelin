package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import com.javarush.island.apalinskiy.map.Cell;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Boar extends Herbivore {

    public Boar() {
        super(400, 50, 2, 50);
    }

    @Override
    protected Animal createOffspring() {
        return new Boar();
    }

    @Override
    public void eat() {
        Cell cell = getCurrentCell();
        if (cell == null) {
            return;
        }
        Map<Class<? extends Creature>, Integer> preferences = getFoodPreferences();
        if (preferences == null || preferences.isEmpty()) {
            return;
        }
        for (Creature creature : cell.getCreatures()) {
            if (this == creature) {
                continue;
            }
            if (getCurrentSatiety() >= getSatietySize()) {
                break;
            }
            Integer chance = preferences.get(creature.getClass());
            if (chance == null) {
                continue;
            }
            switch (creature) {
                case AbstractPlant plant -> {
                    int roll = ThreadLocalRandom.current().nextInt(100);
                    if (roll < chance) {
                        setCurrentSatiety(getCurrentSatiety() + plant.getWeight());
                        plant.die();
                    }
                }
                case Caterpillar caterpillar -> {
                    int roll = ThreadLocalRandom.current().nextInt(100);
                    if (roll < chance) {
                        setCurrentSatiety(getCurrentSatiety() + caterpillar.getWeight());
                        caterpillar.die();
                    }
                }
                case Mouse mouse -> {
                    int roll = ThreadLocalRandom.current().nextInt(100);
                    if (roll < chance) {
                        setCurrentSatiety(getCurrentSatiety() + mouse.getWeight());
                        mouse.die();
                    }
                }
                default -> {
                }
            }
        }
    }
}
