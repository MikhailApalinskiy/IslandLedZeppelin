package com.javarush.island.apalinskiy.simulation.worker;

import com.javarush.island.apalinskiy.creatures.animals.Animal;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class AnimalWorker implements Runnable {
    private final List<Animal> animals;
    private final ReentrantLock lock;
    private final Condition condition;

    public AnimalWorker(List<Animal> animals, ReentrantLock lock, Condition condition) {
        this.animals = animals;
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            Iterator<Animal> iterator = animals.iterator();
            while (iterator.hasNext()) {
                Animal animal = iterator.next();
                if (!animal.isAlive()) {
                    iterator.remove();
                    continue;
                }
                animal.move();
                animal.eat();
                animal.reproduce();
            }
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
