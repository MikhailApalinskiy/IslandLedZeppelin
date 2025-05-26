package com.javarush.island.apalinskiy.util;

import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.map.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapUtils {
    private static Map globalMap;

    private static final HashMap<String, List<Cell>> cache = new HashMap<>();

    public static void init(Map map) {
        globalMap = map;
    }

    public static List<Cell> getNeighborsInRange(Cell cell, int moveRange) {
        String key = cell.getX() + ":" + cell.getY() + ":" + moveRange;
        if (cache.containsKey(key)) {
            return cache.get(key);
        }
        List<Cell> neighbors = new ArrayList<>();
        int x = cell.getX();
        int y = cell.getY();
        Cell[][] grid = globalMap.getMap();
        int length = globalMap.getLength();
        int width = globalMap.getWidth();
        for (int i = 1; i <= moveRange; i++) {
            if (x - i >= 0) {
                neighbors.add(grid[x - i][y]);
            }
            if (x + i < length) {
                neighbors.add(grid[x + i][y]);
            }
            if (y - i >= 0) {
                neighbors.add(grid[x][y - i]);
            }
            if (y + i < width) {
                neighbors.add(grid[x][y + i]);
            }
        }
        cache.put(key, neighbors);
        return neighbors;
    }
}
