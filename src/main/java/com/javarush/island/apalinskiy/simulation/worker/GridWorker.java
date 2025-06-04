package com.javarush.island.apalinskiy.simulation.worker;

import com.javarush.island.apalinskiy.map.Cell;

import java.util.concurrent.Phaser;

/**
 * A worker responsible for processing a block of rows in the simulation grid.
 * <p>
 * Unlike per-cell threading, this design assigns a continuous range of rows
 * (a horizontal slice of the map) to each {@code GridWorker}, which significantly
 * reduces thread count and improves performance and synchronization.
 * <p>
 * A {@link java.util.concurrent.Phaser} is used to coordinate simulation steps across all workers,
 * ensuring consistent execution through the following three phases:
 * <ol>
 *   <li>Processing animal and plant behavior in assigned cells.</li>
 *   <li>Applying collected changes to shared cell collections.</li>
 *   <li>Triggering map statistics collection and UI updates.</li>
 * </ol>
 * This phased, lock-free approach provides safe concurrent execution without explicit locking.
 */
public class GridWorker implements Runnable {
    private final Cell[][] grid;
    private final int startRow;
    private final int endRow;
    private final Phaser phaser;

    public GridWorker(Cell[][] grid, int startRow, int endRow, Phaser phaser) {
        this.grid = grid;
        this.startRow = startRow;
        this.endRow = endRow;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                for (int y = startRow; y < endRow; y++) {
                    for (int x = 0; x < grid[0].length; x++) {
                        grid[y][x].processLogicStep();
                    }
                }
                phaser.arriveAndAwaitAdvance();
                for (int y = startRow; y < endRow; y++) {
                    for (int x = 0; x < grid[0].length; x++) {
                        grid[y][x].processCleanupStep();
                    }
                }
                phaser.arriveAndAwaitAdvance();
                phaser.arriveAndAwaitAdvance();
            }
        } finally {
            phaser.arriveAndDeregister();
        }
    }
}
