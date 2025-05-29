package com.javarush.island.apalinskiy.repository;

import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;

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

    public static List<AbstractPlant> getPlantsOfType(Class<? extends AbstractPlant> type) {
        return registry.getOrDefault(type, List.of());
    }
}
