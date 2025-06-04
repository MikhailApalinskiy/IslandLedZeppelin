package com.javarush.island.apalinskiy.util;

import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.map.Map;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utility class providing map-related helper methods for the simulation.
 * <p>
 * It allows efficient retrieval of neighboring cells within a given range,
 * used primarily by animals during movement logic.
 * <p>
 * To reduce repeated calculations and object allocations, a thread-safe
 * cache is maintained for neighbor lookup results based on cell coordinates
 * and movement range.
 * <p>
 * This class assumes the simulation map is initialized once via {@link #init(Map)}
 * and remains unchanged throughout the simulation.
 * <p><b>Thread safety:</b> read operations and cache usage are thread-safe.
 */
public class MapUtils {
    private static volatile Map map;

    private static final ConcurrentHashMap<String, List<Cell>> cache = new ConcurrentHashMap<>();

    public static void init(Map simulationMap) {
        map = simulationMap;
    }

    public static List<Cell> getNeighborsInRange(Cell cell, int moveRange) {
        String key = cell.getX() + ":" + cell.getY() + ":" + moveRange;
        return cache.computeIfAbsent(key, k -> {
            List<Cell> neighbors = new ArrayList<>();
            int x = cell.getX();
            int y = cell.getY();
            Cell[][] grid = map.getMap();
            int length = map.getLength();
            int width = map.getWidth();
            for (int i = 1; i <= moveRange; i++) {
                if (x - i >= 0) neighbors.add(grid[x - i][y]);
            }
            for (int i = 1; i <= moveRange; i++) {
                if (x + i < length) neighbors.add(grid[x + i][y]);
            }
            for (int i = 1; i <= moveRange; i++) {
                if (y - i >= 0) neighbors.add(grid[x][y - i]);
            }
            for (int i = 1; i <= moveRange; i++) {
                if (y + i < width) neighbors.add(grid[x][y + i]);
            }
            return neighbors;
        });
    }
}
