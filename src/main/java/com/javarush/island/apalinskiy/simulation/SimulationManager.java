package com.javarush.island.apalinskiy.simulation;

import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.map.Map;
import com.javarush.island.apalinskiy.simulation.worker.CellWorker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class SimulationManager {
    private final ExecutorService executor;
    private final Phaser phaser;
    private final Map map;

    public SimulationManager(Map map, Phaser phaser) {
        this.executor = Executors.newFixedThreadPool(map.getLength() * map.getWidth());
        this.map = map;
        this.phaser = phaser;
    }

    public void run() {
        Cell[][] grid = map.getMap();
        for (Cell[] cells : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                executor.submit(new CellWorker(cells[j], phaser));
            }
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
