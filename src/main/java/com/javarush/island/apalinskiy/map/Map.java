package com.javarush.island.apalinskiy.map;

import lombok.Getter;

/**
 * Represents the core simulation map consisting of a two-dimensional grid of {@link Cell} objects.
 * <p>
 * The map dimensions can be customized via the {@code length} and {@code width} fields,
 * allowing for flexible adjustment of the simulation field size.
 * Each cell is initialized via {@link #initializeMap()}, which populates the {@code map} array with new {@code Cell} instances.
 * <p>
 * This class serves as the central structure for storing and accessing simulation state, and is shared
 * across worker threads during the simulation's step-by-step execution.
 * <p>
 * Thread-safety is ensured not through internal synchronization in this class,
 * but via the external control flow of the simulation (e.g., {@link java.util.concurrent.Phaser}).
 */
@Getter
public class Map {
    /**
     * The length (number of columns) of the map grid.
     * <p>
     * This value can be modified to configure the horizontal size of the field.
     */
    private final int length = 100;
    /**
     * The width (number of rows) of the map grid.
     * <p>
     * This value can be modified to configure the vertical size of the field.
     */
    private final int width = 20;
    private final Cell[][] map = new Cell[length][width];

    public void initializeMap() {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                map[x][y] = new Cell(x, y);
            }
        }
    }
}
