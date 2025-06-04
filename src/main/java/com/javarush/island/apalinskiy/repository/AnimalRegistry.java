package com.javarush.island.apalinskiy.repository;

import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.animals.herbivores.*;
import com.javarush.island.apalinskiy.creatures.animals.predators.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Thread-safe repository for managing all animal instances grouped by type.
 * <p>
 * Used primarily for collecting simulation statistics.
 * <p>
 * Animals are registered during initialization or reproduction,
 * and the registry supports concurrent access from simulation threads.
 */
public class AnimalRegistry {
    private static final Map<Class<? extends Animal>, List<Animal>> registry = new ConcurrentHashMap<>();

    public static void register(Animal animal) {
        registry.computeIfAbsent(animal.getClass(), k -> Collections.synchronizedList(new ArrayList<>()))
                .add(animal);
    }

    public static void unregister(Animal animal) {
        List<Animal> list = registry.get(animal.getClass());
        if (list != null) {
            synchronized (list) {
                list.remove(animal);
            }
        }
    }

    public static List<List<Animal>> getAllAnimalGroups() {
        return new ArrayList<>(registry.values());
    }

    public static List<Class<? extends Animal>> getAllAnimalTypes() {
        return List.of(Boar.class, Buffalo.class, Caterpillar.class, Deer.class, Duck.class,
                Goat.class, Horse.class, Mouse.class, Rabbit.class, Sheep.class, Bear.class,
                Boa.class, Eagle.class, Fox.class, Wolf.class);
    }
}
