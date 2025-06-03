package com.javarush.island.apalinskiy.simulation;

import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.map.Map;
import com.javarush.island.apalinskiy.simulation.worker.GridWorker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class SimulationManager {
    private final ExecutorService executor;
    private final Phaser phaser;
    private final Map map;
    private final int numWorkers;

    public SimulationManager(Map map, Phaser phaser) {
        this.map = map;
        this.phaser = phaser;
        this.numWorkers = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(numWorkers);
    }

    public void run() {
        Cell[][] grid = map.getMap();
        int totalRows = grid.length;
        int rowsPerWorker = (int) Math.ceil((double) totalRows / numWorkers);
        for (int i = 0; i < numWorkers; i++) {
            int startRow = i * rowsPerWorker;
            int endRow = Math.min(startRow + rowsPerWorker, totalRows);
            phaser.register();
            executor.submit(new GridWorker(grid, startRow, endRow, phaser));
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
