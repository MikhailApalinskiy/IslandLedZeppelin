package com.javarush.island.apalinskiy.statistics;

import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import com.javarush.island.apalinskiy.repository.AnimalRegistry;
import com.javarush.island.apalinskiy.repository.PlantRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimulationStatistics {
    public static Map<String, Integer> collectAnimalStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        for (List<Animal> animalGroup : AnimalRegistry.getAllAnimalGroups()) {
            if (!animalGroup.isEmpty()) {
                String type = animalGroup.getFirst().getClass().getSimpleName();
                stats.put(type, animalGroup.size());
            }
        }
        return stats;
    }

    public static Map<String, Integer> collectPlantStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        for (List<AbstractPlant> plantGroup : PlantRegistry.getAllPlantGroups()) {
            if (!plantGroup.isEmpty()) {
                String type = plantGroup.getFirst().getClass().getSimpleName();
                stats.put(type, plantGroup.size());
            }
        }
        return stats;
    }

    public static void printStatistics() {
        System.out.println("=== Animal Statistics ===");
        collectAnimalStatistics().forEach((type, count) ->
                System.out.printf("%-15s: %d%n", type, count));
        System.out.println("\n=== Plant Statistics ===");
        collectPlantStatistics().forEach((type, count) ->
                System.out.printf("%-15s: %d%n", type, count));
    }
}
