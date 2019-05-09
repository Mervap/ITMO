package ru.ifmo.rain.teplyakov.walk;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class FileVisitor extends SimpleFileVisitor<Path> {

    private final BufferedWriter writer;
    private static final int FNV_PRIME = 0x01000193;
    private static final int BUFF_SIZE = 1024 * 4;

    FileVisitor(BufferedWriter writer) {
        this.writer = writer;
    }

    private FileVisitResult write(int hash, String path) throws IOException {
        writer.write(String.format("%08x %s%n", hash, path));
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        int hash = 0x811c9dc5;

        try (InputStream reader = Files.newInputStream(file)) {
            int cnt;
            byte[] buf = new byte[BUFF_SIZE];
            while ((cnt = reader.read(buf)) != -1) {
                for (int i = 0; i < cnt; ++i) {
                    hash *= FNV_PRIME;
                    hash ^= buf[i] & 255;
                }
            }
        } catch (IOException e) {
            hash = 0;
        }

        return write(hash, file.toString());
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
        return write(0, file.toString());
    }
}
