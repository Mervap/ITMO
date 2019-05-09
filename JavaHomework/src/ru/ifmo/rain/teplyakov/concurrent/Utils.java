package ru.ifmo.rain.teplyakov.concurrent;

import java.util.List;

public class Utils {

    /**
     * Add {@code thread} to thread store {@code workers} and start {@code thread}
     *
     * @param workers {@link List} of {@link Thread}s in which need to add {@code thread}
     * @param thread  {@link Thread} which need to add and start
     */
    public static void addAndStartThread(List<Thread> workers, Thread thread) {
        workers.add(thread);
        thread.start();
    }

    /**
     * Checks that number of threads is positive
     *
     * @param threads number of threads
     * @throws IllegalArgumentException if number not positive
     */
    public static void checkNumberOfThreads(int threads) {
        if (threads < 1) {
            throw new IllegalArgumentException("Number of threads must be positive");
        }
    }
}
