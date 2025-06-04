package com.javarush.island.apalinskiy.map;

import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.creatures.animals.Animal;
import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import lombok.Getter;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a single cell on the simulation map, holding collections of {@link Animal} and {@link AbstractPlant} instances.
 * <p>
 * Each cell maintains its own state, including a lifecycle counter for animals and plants,
 * and queues for newly created entities to be merged in a separate cleanup phase.
 * <p>
 * Thread-safety is maintained externally via phased step-by-step synchronization;
 * therefore, no explicit locking is required inside the cell itself.
 * <p>
 * This class overrides {@code equals()} and {@code hashCode()}.
 * These methods were originally implemented to prevent deadlocks
 * when {@code Cell} instances were stored in {@code HashSet} during
 * concurrent simulations using locking mechanisms.
 * <p>
 * After refactoring the simulation to a phased model with
 * {@code Phaser}-based synchronization and removal of explicit locks,
 * the need for these overrides was eliminated.
 * However, they are retained in case future modifications
 * require {@code Cell} to be used in hash-based collections or
 * identity-based comparisons again.
 */
@Getter
public class Cell {
    private final int plantLifeCycle = 6;// Max ticks before a plant dies naturally
    private final int animaLifeCycle = 120;// Max ticks before an animal dies naturally
    private final int x;
    private final int y;
    private final ArrayList<Animal> animals = new ArrayList<>();
    private final ArrayList<AbstractPlant> plants = new ArrayList<>();
    /**
     * Thread-safe buffers for newly created entities (offspring).
     * <p>
     * During Phase 1 of the simulation, animals and plants reproduce and their offspring
     * are temporarily stored here. These collections are then merged into the main lists
     * during Phase 2.
     * <p>
     * The use of {@code ConcurrentLinkedQueue} ensures safe concurrent additions
     * without explicit locking.
     */
    private final Queue<Animal> newAnimals = new ConcurrentLinkedQueue<>();
    private final Queue<AbstractPlant> newPlants = new ConcurrentLinkedQueue<>();

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void addNewAnimal(Animal animal) {
        newAnimals.add(animal);
        animal.setCurrentCell(this);
    }

    public void addAnimal(Animal animal) {
        animals.add(animal);
        animal.setCurrentCell(this);
    }

    public void addNewPlant(AbstractPlant plant) {
        newPlants.add(plant);
        plant.setCurrentCell(this);
    }

    public void addPlant(AbstractPlant plant) {
        plants.add(plant);
        plant.setCurrentCell(this);
    }

    public Set<Creature> getCreatures() {
        Set<Creature> all = new HashSet<>();
        all.addAll(animals);
        all.addAll(plants);
        return all;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Cell cell)) return false;
        return x == cell.x && y == cell.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Executes one simulation logic step:
     * - Animals eat, reproduce, and move
     * - Plants reproduce
     * - Life ticks are incremented and checked for natural death
     * <p>
     * This method is called in Phase 1 of the simulation cycle.
     */
    public void processLogicStep() {
        for (Animal animal : animals) {
            if (!animal.isAlive()) {
                continue;
            }
            int i = animal.getTikCount();
            i++;
            animal.setTikCount(i);
            animal.eat();
            if (animal.getTikCount() >= animaLifeCycle) {
                animal.die();
            }
            if (animal.getCurrentSatiety() == 0) {
                animal.die();
            }
            int animalReproduceChance = 30;
            if (ThreadLocalRandom.current().nextInt(100) < animalReproduceChance) {
                Animal offspring = animal.reproduce();
                if (offspring != null) {
                    addNewAnimal(offspring);
                }
            }
            animal.move();
        }
        for (AbstractPlant plant : plants) {
            if (!plant.isAlive()) {
                continue;
            }
            int i = plant.getTickCounter();
            i++;
            plant.setTickCounter(i);
            int plantReproduceChance = 20;
            if (ThreadLocalRandom.current().nextInt(100) < plantReproduceChance) {
                AbstractPlant offSpring = plant.reproduce();
                if (offSpring != null && offSpring.getCurrentCell() == this) {
                    addNewPlant(offSpring);
                }
            }
            if (i >= plantLifeCycle) {
                plant.die();
            }
        }
    }

    /**
     * Applies buffered changes from Phase 1:
     * - Removes dead entities
     * - Integrates newly born animals and plants
     * <p>
     * Called in Phase 2 of the simulation cycle.
     */
    public void processCleanupStep() {
        animals.removeIf(a -> !a.isAlive() || a.getCurrentCell() != this);
        animals.addAll(newAnimals);
        newAnimals.clear();
        plants.removeIf(p -> !p.isAlive());
        plants.addAll(newPlants);
        newPlants.clear();
    }
}
