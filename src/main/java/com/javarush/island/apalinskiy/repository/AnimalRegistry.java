package com.javarush.island.apalinskiy.repository;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnimalRegistry {
    private static final Map<Class<? extends Animal>, List<Animal>> registry = new ConcurrentHashMap<>();

    public static List<Animal> getAnimalsOfType(Class<? extends Animal> type) {
        return registry.getOrDefault(type, List.of());
    }
}
