package com.javarush.island.apalinskiy.statistics;

import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import com.javarush.island.apalinskiy.repository.AnimalRegistry;
import com.javarush.island.apalinskiy.repository.PlantRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for collecting and formatting statistics about the current state of the simulation.
 * <p>
 * It gathers counts of all animals and plants registered in the simulation by accessing
 * {@link AnimalRegistry} and {@link PlantRegistry}, and uses their emoji representations
 * as display-friendly keys.
 * <p>
 * The class assumes that all entities are properly registered and that the registries
 * are kept up-to-date by the simulation logic.
 * <p>
 * Thread-safe in practice due to underlying concurrent registries, but not synchronized internally.
 * Intended for read-only usage during simulation pause phases (between steps).
 */
public class SimulationStatistics {
    public static Map<String, Integer> collectAnimalStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        for (List<Animal> animalGroup : AnimalRegistry.getAllAnimalGroups()) {
            if (!animalGroup.isEmpty()) {
                Animal sample = animalGroup.getFirst();
                String emoji = sample.getEmoji();
                stats.put(emoji, animalGroup.size());
            }
        }
        return stats;
    }

    public static Map<String, Integer> collectPlantStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        for (List<AbstractPlant> plantGroup : PlantRegistry.getAllPlantGroups()) {
            if (!plantGroup.isEmpty()) {
                AbstractPlant sample = plantGroup.getFirst();
                String emoji = sample.getEmoji();
                stats.put(emoji, plantGroup.size());
            }
        }
        return stats;
    }

    public static String getStatisticsText() {
        StringBuilder sb = new StringBuilder();
        collectAnimalStatistics().forEach((emoji, count) ->
                sb.append(String.format("%s : %d\n", emoji, count)));
        collectPlantStatistics().forEach((emoji, count) ->
                sb.append(String.format("%s : %d\n", emoji, count)));
        return sb.toString();
    }
}
