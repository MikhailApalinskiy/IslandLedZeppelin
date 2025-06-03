package com.javarush.island.apalinskiy.simulation.worker;

import com.javarush.island.apalinskiy.map.Cell;

import java.util.concurrent.Phaser;

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
