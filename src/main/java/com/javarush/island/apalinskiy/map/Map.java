package com.javarush.island.apalinskiy.map;

import lombok.Getter;

@Getter
public class Map {
    private final int length = 100;
    private final int width = 20;
    private final Cell[][] map = new Cell[length][width];

    public void initializeMap(){
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = new Cell(i, j);
            }
        }
    }
}
