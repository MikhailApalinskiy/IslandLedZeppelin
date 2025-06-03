package com.javarush.island.apalinskiy.creatures.animals.herbivores;

import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import com.javarush.island.apalinskiy.creatures.plants.Plant;
import com.javarush.island.apalinskiy.map.Cell;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Herbivore extends Animal {
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
