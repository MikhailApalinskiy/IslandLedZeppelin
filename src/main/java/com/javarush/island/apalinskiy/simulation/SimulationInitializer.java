package com.javarush.island.apalinskiy.simulation;

import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.map.Map;
import com.javarush.island.apalinskiy.repository.AnimalRegistry;
import com.javarush.island.apalinskiy.repository.PlantRegistry;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SimulationInitializer {
    private final Map map;

    public SimulationInitializer(Map map) {
        this.map = map;
    }

    public void randomlyPopulateAnimals() {
        Cell[][] greedMap = map.getMap();
        int rows = greedMap.length;
        int cols = greedMap[0].length;
        List<Class<? extends Animal>> animalTypes = AnimalRegistry.getAllAnimalTypes();
        for (Class<? extends Animal> animalClass : animalTypes) {
            try {
                Animal prototype = animalClass.getDeclaredConstructor().newInstance();
                int flockSize = prototype.getFlockSize();
                int animalFactor = 10;
                int totalToPlace = flockSize * animalFactor;
                int placed = 0;
                while (placed < totalToPlace) {
                    int x = ThreadLocalRandom.current().nextInt(rows);
                    int y = ThreadLocalRandom.current().nextInt(cols);
                    Cell cell = greedMap[x][y];
                    long sameTypeCount = cell.getAnimals().stream()
                            .filter(a -> a.getClass() == animalClass)
                            .count();
                    if (sameTypeCount < flockSize) {
                        Animal animal = animalClass.getDeclaredConstructor().newInstance();
                        AnimalRegistry.register(animal);
                        cell.addAnimal(animal);
                        placed++;
                    }
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void randomlyPopulatePlants() {
        Cell[][] gridMap = map.getMap();
        int rows = gridMap.length;
        int cols = gridMap[0].length;
        List<Class<? extends AbstractPlant>> plantTypes = PlantRegistry.getAllPlantTypes();
        for (Class<? extends AbstractPlant> plantClass : plantTypes) {
            try {
                AbstractPlant prototype = plantClass.getDeclaredConstructor().newInstance();
                int flockSize = prototype.getFlockSize();
                int plantFactor = 100;
                int totalToPlace = flockSize * plantFactor;
                int placed = 0;
                while (placed < totalToPlace) {
                    int x = ThreadLocalRandom.current().nextInt(rows);
                    int y = ThreadLocalRandom.current().nextInt(cols);
                    Cell cell = gridMap[x][y];
                    long sameTypeCount = cell.getPlants().stream()
                            .filter(p -> p.getClass() == plantClass)
                            .count();
                    if (sameTypeCount < flockSize) {
                        AbstractPlant plant = plantClass.getDeclaredConstructor().newInstance();
                        PlantRegistry.register(plant);
                        cell.addPlant(plant);
                        placed++;
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
