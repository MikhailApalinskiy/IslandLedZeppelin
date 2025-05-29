package com.javarush.island.apalinskiy.repository;

import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlantRegistry {
    private static final Map<Class<? extends AbstractPlant>, List<AbstractPlant>> registry = new ConcurrentHashMap<>();

    public static List<AbstractPlant> getPlantsOfType(Class<? extends AbstractPlant> type) {
        return registry.getOrDefault(type, List.of());
    }
}
