package com.javarush.island.apalinskiy.map;

import lombok.Getter;

@Getter
public class Map {
    private final int length = 100;
    private final int width = 20;
    private final Cell[][] map = new Cell[length][width];

    public void initializeMap(){
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                map[x][y] = new Cell(x, y);
            }
        }
    }
}
