package ru.ifmo.rain.teplyakov.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

import static ru.ifmo.rain.teplyakov.concurrent.Utils.addAndStartThread;
import static ru.ifmo.rain.teplyakov.concurrent.Utils.checkNumberOfThreads;

public class ParallelMapperImpl implements ParallelMapper {

    private final Queue<Runnable> tasks;
    private final List<Thread> workers;

    /**
     * Creates new instance of {@link ParallelMapperImpl} with {@code Thread Pool} size {@code threads}.
     */
    public ParallelMapperImpl(int threads) {
        checkNumberOfThreads(threads);

        tasks = new ArrayDeque<>();
        workers = new ArrayList<>();
        for (int i = 0; i < threads; ++i) {
            addAndStartThread(workers, new Thread(() -> {
                try {
                    while (!Thread.interrupted()) {
                        solveTask();
                    }
                } catch (InterruptedException e) {
                    // ignored
                } finally {
                    Thread.currentThread().interrupt();
                }
            }));
        }
    }

    private void solveTask() throws InterruptedException {
        Runnable task;
        synchronized (tasks) {
            while (tasks.isEmpty()) {
                tasks.wait();
            }
            task = tasks.poll();
        }

        task.run();
    }

    private void addTask(Runnable run) {
        synchronized (tasks) {
            tasks.add(run);
            tasks.notify();
        }
    }

    private class MapResult<R> {
        List<R> result;
        RuntimeException e;
        int cnt;

        MapResult(int cnt) {
            this.cnt = cnt;
            this.e = null;
            result = new ArrayList<>(Collections.nCopies(cnt, null));
        }

        synchronized void set(int pos, R data) {
            result.set(pos, data);
            if (--cnt == 0) {
                notify();
            }
        }

        synchronized void setException(RuntimeException e) {
            this.e = e;
        }

        synchronized List<R> get() throws InterruptedException {
            while (cnt != 0) {
                wait();
            }

            if (e != null) {
                throw e;
            }
            return result;
        }
    }

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> function, List<? extends T> list) throws InterruptedException {
        MapResult<R> res = new MapResult<>(list.size());
        for (int i = 0; i < list.size(); i++) {
            final int pos = i;
            addTask(() -> {
                try {
                    res.set(pos, function.apply(list.get(pos)));
                } catch (RuntimeException e) {
                    res.setException(e);
                }
            });
        }

        return res.get();
    }

    @Override
    public void close() {
        workers.forEach(Thread::interrupt);

        for (Thread thread : workers) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                // ignored
            }
        }
    }
}
