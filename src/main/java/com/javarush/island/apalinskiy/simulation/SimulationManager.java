package com.javarush.island.apalinskiy.simulation;

import com.javarush.island.apalinskiy.map.Cell;
import com.javarush.island.apalinskiy.map.Map;
import com.javarush.island.apalinskiy.simulation.worker.GridWorker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

/**
 * Manages execution of the simulation in a step-by-step, multi-threaded fashion.
 * <p>
 * The simulation is split into three well-defined phases per cycle:
 * <ol>
 *   <li><b>Phase 1</b>: Processing of all animals and plants inside their assigned {@code Cell} blocks.
 *       Reproduction and movement are recorded into thread-safe buffers.</li>
 *   <li><b>Phase 2</b>: Consolidation of the temporary changes back into the main cell structures.</li>
 *   <li><b>Phase 3</b>: Triggering of UI updates and statistical output via {@code Platform.runLater} or console.</li>
 * </ol>
 *
 * <p>
 * Threads do not manage species, but instead process blocks of map rows ({@code Cell[][]}),
 * which promotes spatial locality and simplifies synchronization.
 *
 * <p>
 * Uses {@link java.util.concurrent.Phaser} for global coordination of all worker threads
 * and the main thread. This design eliminates the need for explicit locks (e.g. {@code synchronized} or {@code ReentrantLock}),
 * which improves performance and prevents deadlocks or resource contention.
 *
 * <p>
 * The number of worker threads is based on the number of available processors,
 * and they are managed using a fixed thread pool.
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
