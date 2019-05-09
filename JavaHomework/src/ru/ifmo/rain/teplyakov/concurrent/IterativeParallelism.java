package ru.ifmo.rain.teplyakov.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.ifmo.rain.teplyakov.concurrent.Utils.addAndStartThread;
import static ru.ifmo.rain.teplyakov.concurrent.Utils.checkNumberOfThreads;

public class IterativeParallelism implements ListIP {

    private final ParallelMapper mapper;

    /**
     * Creates new instance of {@link IterativeParallelism} without {@link ParallelMapper}.
     */
    public IterativeParallelism() {
        mapper = null;
    }

    /**
     * Creates new instance of {@link IterativeParallelism} using {@link ParallelMapper}
     * which was passed as a parameter {@code mapper}.
     */
    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }

    private void joinThreads(List<Thread> workers) throws InterruptedException {
        InterruptedException exception = null;
        for (Thread thread : workers) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                if (exception == null) {
                    exception = new InterruptedException("Not all threads joined");
                }
                exception.addSuppressed(e);
            }
        }

        if (exception != null) {
            throw exception;
        }
    }

    private <T, R> R base(int threads, List<? extends T> values,
                          Function<? super Stream<? extends T>, ? extends R> task,
                          Function<? super Stream<? extends R>, ? extends R> taskMerger) throws InterruptedException {

        checkNumberOfThreads(threads);
        threads = Math.max(1, Math.min(threads, values.size()));
        List<Stream<? extends T>> subTasks = new ArrayList<>();
        int subCnt = values.size() / threads;
        int shouldAdd = values.size() % threads;

        for (int i = 0, l = 0; i < threads; ++i) {
            int r = l + subCnt + (shouldAdd > i ? 1 : 0);
            subTasks.add(values.subList(l, r).stream());
            l = r;
        }

        List<R> res;
        if (mapper == null) {
            List<Thread> workers = new ArrayList<>();
            res = new ArrayList<>(Collections.nCopies(threads, null));

            for (int i = 0; i < threads; ++i) {
                final int pos = i;
                addAndStartThread(workers, new Thread(() -> res.set(pos, task.apply(subTasks.get(pos)))));
            }

            joinThreads(workers);
        } else {
            res = mapper.map(task, subTasks);
        }
        return taskMerger.apply(res.stream());
    }

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        if (values.isEmpty()) {
            throw new NoSuchElementException("Value's list should be not empty");
        }

        final Function<Stream<? extends T>, ? extends T> max = stream -> stream.max(comparator).get();
        return base(threads, values, max, max);
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, Collections.reverseOrder(comparator));
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return base(threads, values,
                stream -> stream.allMatch(predicate),
                stream -> stream.allMatch(Boolean::booleanValue));
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return !all(threads, values, elem -> !predicate.test(elem));
    }

    @Override
    public String join(int threads, List<?> values) throws InterruptedException {
        return base(threads, values,
                stream -> stream.map(Object::toString).collect(Collectors.joining()),
                stream -> stream.collect(Collectors.joining()));
    }

    @Override
    public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return base(threads, values,
                stream -> stream.filter(predicate).collect(Collectors.toList()),
                stream -> stream.flatMap(Collection::stream).collect(Collectors.toList()));
    }

    @Override
    public <T, R> List<R> map(int threads, List<? extends T> values, Function<? super T, ? extends R> function) throws InterruptedException {
        return base(threads, values,
                stream -> stream.map(function).collect(Collectors.toList()),
                stream -> stream.flatMap(Collection::stream).collect(Collectors.toList()));
    }
}