package com.javarush.island.apalinskiy.creatures.animals;

import com.javarush.island.apalinskiy.entity.Killable;
import com.javarush.island.apalinskiy.entity.Moveable;
import com.javarush.island.apalinskiy.creatures.Creature;
import com.javarush.island.apalinskiy.entity.Eatable;
import com.javarush.island.apalinskiy.entity.Reproducible;
import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.repository.AnimalRegistry;
import com.javarush.island.apalinskiy.util.MapUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Base class for all animal entities in the simulation.
 * <p>
 * Implements {@link Eatable}, {@link Moveable}, {@link Reproducible}, and {@link Killable},
 * enabling each animal to perform core life functions: eating, moving, reproducing, and dying.
 * <p>
 * Each animal has a unique {@code serialNumber}, used in {@code equals()} and {@code hashCode()}.
 * These overrides were originally necessary for identity-based management in hash-based
 * collections (such as {@link java.util.HashSet}) during earlier designs of the simulation.
 * <p>
 * Since the simulation was refactored into a step-by-step model where threads process
 * map cells (rather than species), and synchronized collections replaced sets,
 * the original need for these overrides was eliminated. However, they are retained
 * for potential future scenarios involving identity-sensitive structures.
 * <p>
 * The {@code emoji} field is used for visual representation in the JavaFX interface.
 */
@Setter
@Getter
public abstract class Animal extends Creature implements Eatable, Moveable, Reproducible<Animal>, Killable {
    private final double weight;
    private final double satietySize;
    private final int speed;
    private final int flockSize;
    private final int serialNumber;
    private double currentSatiety;
    private volatile boolean isAlive = true;
    private Cell currentCell;
    private final String emoji;
    private int tikCount = 0;

    private static int counter = 0;

    protected Animal(double weight, double satietySize, int speed, int flockSize, String emoji) {
        this.weight = weight;
        this.satietySize = satietySize;
        this.speed = speed;
        this.flockSize = flockSize;
        this.serialNumber = ++counter;
        this.currentSatiety = 0;
        this.emoji = emoji;
    }

    protected abstract Animal createOffspring();

    @Override
    public void die() {
        isAlive = false;
        setCurrentCell(null);
        AnimalRegistry.unregister(this);
    }

    @Override
    public void move() {
        Cell from = getCurrentCell();
        if (from == null) {
            return;
        }
        List<Cell> neighbors = MapUtils.getNeighborsInRange(from, getSpeed());
        Collections.shuffle(neighbors);
        for (Cell to : neighbors) {
            int count = 0;
            for (Animal a : to.getAnimals()) {
                if (a.getClass() == this.getClass()) count++;
            }
            for (Animal a : to.getNewAnimals()) {
                if (a.getClass() == this.getClass()) count++;
            }
            if (count < getFlockSize()) {
                to.addNewAnimal(this);
                setCurrentCell(to);
                return;
            }
        }
    }

    @Override
    public Animal reproduce() {
        Cell cell = getCurrentCell();
        if (cell == null) {
            return null;
        }
        int count = 0;
        Animal mate = null;
        for (Animal animal : cell.getAnimals()) {
            if (animal.getClass() == this.getClass()) {
                count++;
                if (animal != this && mate == null) {
                    mate = animal;
                }
            }
        }
        count += cell.getNewAnimals().size();
        if (count >= getFlockSize() || mate == null) {
            return null;
        }
        Animal offspring = createOffspring();
        AnimalRegistry.register(offspring);
        return offspring;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Animal other)) return false;
        return serialNumber == other.serialNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(serialNumber);
    }
}
