package com.javarush.island.apalinskiy.creatures.animals.predators;

import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.animals.herbivores.*;
import com.javarush.island.apalinskiy.map.Cell;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Abstract base class for all carnivorous (predator) animals in the simulation.
 * <p>
 * Defines species-specific food preferences using a static {@code foodMap},
 * where each predator type is mapped to a set of prey types with corresponding
 * success probabilities.
 * <p>
 * The {@code eat()} method implements basic predation logic using these preferences,
 * random chance, and current satiety levels.
 * <p>
 * This class is not meant to be instantiated directly, but provides a shared
 * foundation for all predator animals in the ecosystem model.
 */
public abstract class Predator extends Animal {
    /**
     * A static registry of predator food preferences.
     * <p>
     * Maps each predator class to a map of possible prey types with associated
     * chance percentages (0–100) of successful consumption.
     * <p>
     * This central configuration allows all predator species to share and follow
     * consistent predation logic in the {@code eat()} method.
     */
    protected static final Map<Class<? extends Creature>, Map<Class<? extends Creature>, Integer>> foodMap = new HashMap<>();

    static {
        Map<Class<? extends Creature>, Integer> wolfFood = new HashMap<>();
        wolfFood.put(Horse.class, 10);
        wolfFood.put(Deer.class, 15);
        wolfFood.put(Rabbit.class, 60);
        wolfFood.put(Mouse.class, 80);
        wolfFood.put(Goat.class, 60);
        wolfFood.put(Sheep.class, 70);
        wolfFood.put(Boar.class, 15);
        wolfFood.put(Buffalo.class, 10);
        wolfFood.put(Duck.class, 40);
        foodMap.put(Wolf.class, wolfFood);

        Map<Class<? extends Creature>, Integer> boaFood = new HashMap<>();
        boaFood.put(Fox.class, 15);
        boaFood.put(Rabbit.class, 20);
        boaFood.put(Mouse.class, 40);
        boaFood.put(Duck.class, 10);
        foodMap.put(Boa.class, boaFood);

        Map<Class<? extends Creature>, Integer> foxFood = new HashMap<>();
        foxFood.put(Rabbit.class, 70);
        foxFood.put(Mouse.class, 90);
        foxFood.put(Duck.class, 60);
        foxFood.put(Caterpillar.class, 40);
        foodMap.put(Fox.class, foxFood);

        Map<Class<? extends Creature>, Integer> bearFood = new HashMap<>();
        bearFood.put(Boa.class, 80);
        bearFood.put(Horse.class, 40);
        bearFood.put(Deer.class, 80);
        bearFood.put(Rabbit.class, 80);
        bearFood.put(Mouse.class, 90);
        bearFood.put(Goat.class, 70);
        bearFood.put(Sheep.class, 70);
        bearFood.put(Boar.class, 50);
        bearFood.put(Buffalo.class, 20);
        bearFood.put(Duck.class, 10);
        foodMap.put(Bear.class, bearFood);

        Map<Class<? extends Creature>, Integer> eagleFood = new HashMap<>();
        eagleFood.put(Fox.class, 10);
        eagleFood.put(Rabbit.class, 90);
        eagleFood.put(Mouse.class, 90);
        eagleFood.put(Duck.class, 80);
        foodMap.put(Eagle.class, eagleFood);
    }

    protected Map<Class<? extends Creature>, Integer> getFoodPreferences() {
        return foodMap.get(this.getClass());
    }

    protected Predator(double weight, double satietySize, int speed, int flockSize, String emoji) {
        super(weight, satietySize, speed, flockSize, emoji);
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
            if (creature instanceof Animal animal) {
                int roll = ThreadLocalRandom.current().nextInt(100);
                if (roll < chance) {
                    setCurrentSatiety(getCurrentSatiety() + animal.getWeight());
                    animal.die();
                }
            }
        }
    }
}
