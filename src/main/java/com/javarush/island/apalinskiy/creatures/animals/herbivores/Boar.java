package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Boar extends Herbivore {

    public Boar() {
        super(400, 50, 2, 50);
    }

    @Override
    public void eat() {
        if (getCurrentCell() == null) {
            return;
        }
        Map<Class<? extends Creature>, Integer> preferences = getFoodPreferences();
        if (preferences == null || preferences.isEmpty()) {
            return;
        }
        for (Creature creature : getCurrentCell().getCreatures()) {
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
            if (creature instanceof AbstractPlant plant) {
                int roll = ThreadLocalRandom.current().nextInt(100);
                if (roll < chance) {
                    setCurrentSatiety(getCurrentSatiety() + plant.getWeight());
                    getCurrentCell().removeCreature(creature);
                    plant.die();
                }
            } else if (creature instanceof Caterpillar caterpillar) {
                int roll = ThreadLocalRandom.current().nextInt(100);
                if (roll < chance) {
                    setCurrentSatiety(getCurrentSatiety() + caterpillar.getWeight());
                    getCurrentCell().removeCreature(creature);
                    caterpillar.die();
                }
            } else if (creature instanceof Mouse mouse) {
                int roll = ThreadLocalRandom.current().nextInt(100);
                if (roll < chance) {
                    setCurrentSatiety(getCurrentSatiety() + mouse.getWeight());
                    getCurrentCell().removeCreature(creature);
                    mouse.die();
                }
            }
        }
    }

    @Override
    public void reproduce() {

    }

    @Override
    public void move() {

    }

    @Override
    public void die() {

    }
}
