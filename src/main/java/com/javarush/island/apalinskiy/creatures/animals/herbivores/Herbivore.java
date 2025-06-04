package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import com.javarush.island.apalinskiy.creatures.plants.Plant;
import com.javarush.island.apalinskiy.map.Cell;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Abstract base class for all herbivorous animals in the simulation.
 * <p>
 * Defines food preferences for each herbivore species via a static {@code foodMap},
 * which maps a herbivore class to a set of edible creature types with associated success chances.
 * <p>
 * The {@code eat()} method implements basic plant consumption logic based on current satiety
 * and randomized chance, and uses shared preferences configured statically.
 * <p>
 * This class is not meant to be instantiated directly, but serves as a reusable foundation
 * for all plant-eating creatures.
 */
public abstract class Herbivore extends Animal {
    /**
     * A static registry of herbivore food preferences.
     * <p>
     * Maps each herbivore class to a map of edible plant or creature types,
     * with associated chance percentages (0–100) representing the probability
     * of successful consumption.
     * <p>
     * This configuration supports the logic in the {@code eat()} method
     * for all herbivorous animals in the simulation.
     */
    protected static final Map<Class<? extends Creature>, Map<Class<? extends Creature>, Integer>> foodMap = new HashMap<>();

    static {
        Map<Class<? extends Creature>, Integer> horseFood = new HashMap<>();
        horseFood.put(Plant.class, 100);
        foodMap.put(Horse.class, horseFood);

        Map<Class<? extends Creature>, Integer> deerFood = new HashMap<>();
        deerFood.put(Plant.class, 100);
        foodMap.put(Deer.class, deerFood);

        Map<Class<? extends Creature>, Integer> rabbitFood = new HashMap<>();
        rabbitFood.put(Plant.class, 100);
        foodMap.put(Rabbit.class, rabbitFood);

        Map<Class<? extends Creature>, Integer> mouseFood = new HashMap<>();
        mouseFood.put(Plant.class, 100);
        mouseFood.put(Caterpillar.class, 90);
        foodMap.put(Mouse.class, mouseFood);

        Map<Class<? extends Creature>, Integer> goatFood = new HashMap<>();
        goatFood.put(Plant.class, 100);
        foodMap.put(Goat.class, goatFood);

        Map<Class<? extends Creature>, Integer> sheepFood = new HashMap<>();
        sheepFood.put(Plant.class, 100);
        foodMap.put(Sheep.class, sheepFood);

        Map<Class<? extends Creature>, Integer> boarFood = new HashMap<>();
        boarFood.put(Plant.class, 100);
        boarFood.put(Mouse.class, 50);
        boarFood.put(Caterpillar.class, 90);
        foodMap.put(Boar.class, boarFood);

        Map<Class<? extends Creature>, Integer> buffaloFood = new HashMap<>();
        buffaloFood.put(Plant.class, 100);
        foodMap.put(Buffalo.class, buffaloFood);

        Map<Class<? extends Creature>, Integer> duckFood = new HashMap<>();
        duckFood.put(Plant.class, 100);
        duckFood.put(Caterpillar.class, 90);
        foodMap.put(Duck.class, duckFood);

        Map<Class<? extends Creature>, Integer> caterpillarFood = new HashMap<>();
        caterpillarFood.put(Plant.class, 100);
        foodMap.put(Caterpillar.class, caterpillarFood);
    }

    protected Map<Class<? extends Creature>, Integer> getFoodPreferences() {
        return foodMap.get(this.getClass());
    }

    protected Herbivore(double weight, double satietySize, int speed, int flockSize, String emoji) {
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
            if (creature instanceof AbstractPlant plant) {
                int roll = ThreadLocalRandom.current().nextInt(100);
                if (roll < chance) {
                    setCurrentSatiety(getCurrentSatiety() + plant.getWeight());
                    plant.die();
                }
            }
        }
    }
}
