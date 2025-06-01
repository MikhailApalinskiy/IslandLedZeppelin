package com.javarush.island.apalinskiy.repository;

import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import com.javarush.island.apalinskiy.creatures.plants.Plant;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlantRegistry {
    private static final Map<Class<? extends AbstractPlant>, List<AbstractPlant>> registry = new ConcurrentHashMap<>();

    public static void register(AbstractPlant plant) {
        registry.computeIfAbsent(plant.getClass(), k -> Collections.synchronizedList(new ArrayList<>()))
                .add(plant);
    }

    public static List<List<AbstractPlant>> getAllPlantGroups() {
        return new ArrayList<>(registry.values());
    }

    public static void unregister(AbstractPlant plant) {
        List<AbstractPlant> list = registry.get(plant.getClass());
        if (list != null) {
            synchronized (list) {
                list.remove(plant);
            }
        }
    }

    public static List<Class<? extends AbstractPlant>> getAllPlantTypes() {
        return List.of(Plant.class);
    }
}
