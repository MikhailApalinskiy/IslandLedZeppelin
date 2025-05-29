package com.javarush.island.apalinskiy.simulation.worker;

import com.javarush.island.apalinskiy.creatures.plants.AbstractPlant;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class PlantWorker implements Runnable {
    private final List<AbstractPlant> plants;
    private final ReentrantLock lock;
    private final Condition condition;

    public PlantWorker(List<AbstractPlant> animals, ReentrantLock lock, Condition condition) {
        this.plants = animals;
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        List<AbstractPlant> newbornPlants = new ArrayList<>();
        while (!Thread.currentThread().isInterrupted()) {
            newbornPlants.clear();
            Iterator<AbstractPlant> iterator = plants.iterator();
            while (iterator.hasNext()) {
                AbstractPlant plant = iterator.next();
                if (!plant.isAlive()) {
                    iterator.remove();
                    continue;
                }
                AbstractPlant offSpring = plant.reproduce();
                if (offSpring != null) {
                    newbornPlants.add(offSpring);
                }
            }
            plants.addAll(newbornPlants);
            lock.lock();
            try {
                condition.await();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                lock.unlock();
            }
        }
    }
}
