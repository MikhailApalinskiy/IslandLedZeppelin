package com.javarush.island.apalinskiy.simulation;

import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.map.Map;
import com.javarush.island.apalinskiy.simulation.worker.GridWorker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * This simulation is executed in a step-by-step manner.
 * Threads process grid cell blocks rather than specific animal species.
 * There are three distinct phases per simulation step:
 * <ol>
 *   <li>Processing animals in cells and storing changes in thread-safe collections.</li>
 *   <li>Applying those changes to the main collections inside cells.</li>
 *   <li>Rendering statistics and UI updates.</li>
 * </ol>
 * <p>
 * This design eliminates the need for explicit locking,
 * avoiding potential deadlocks and resource contention.
 * The entire simulation remains synchronized via Phaser phases.
 *
 * <p><strong>⚠ Simulation Performance Warning:</strong></p>
 * <p>
 * This simulation architecture is not optimized for extremely high population density.
 * When the total number of animals exceeds <strong>500,000</strong>,
 * a single simulation tick may take over <strong>1 minute</strong> to process.
 * This is due to:
 * <ul>
 *   <li>Step-by-step iteration through all cells and entities</li>
 *   <li>Per-tick evaluation of reproduction, death, and movement</li>
 *   <li>Accumulated overhead from dynamic collection manipulation</li>
 * </ul>
 * <p>
 * <strong>Suggestions for future optimization:</strong>
 * <ul>
 *   <li>Introduce chunk-based or region-level processing</li>
 *   <li>Skip logic for cells with no active entities</li>
 *   <li>Employ spatial partitioning or entity indexing</li>
 *   <li>Refactor entity logic for better CPU cache locality</li>
 *   <li>Add optional population caps or entity culling mechanisms</li>
 * </ul>
 */
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
