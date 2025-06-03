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
