package com.javarush.island.apalinskiy.repository;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AnimalRegistry {
    private static final Map<Class<? extends Animal>, List<Animal>> registry = new ConcurrentHashMap<>();

    public static void register(Animal animal) {
        registry.computeIfAbsent(animal.getClass(), k -> Collections.synchronizedList(new ArrayList<>()))
                .add(animal);
    }

    public static List<Animal> getAnimalsOfType(Class<? extends Animal> type) {
        return registry.getOrDefault(type, List.of());
    }
}
