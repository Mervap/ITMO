package ru.ifmo.rain.teplyakov.walk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RecursiveWalk {

    private final Path inputPath;
    private final Path outputPath;

    private Path checkPath(String file, String message) throws WalkerException {
        try {
            return Paths.get(file);
        } catch (InvalidPathException e) {
            throw new WalkerException(message + ": " + e.getMessage());
        }
    }

    RecursiveWalk(final String inputFile, final String outputFile) throws WalkerException {
        inputPath = checkPath(inputFile, "Incorrect path to input file");
        outputPath = checkPath(outputFile, "Incorrect path to output file");

        if (outputPath.getParent() != null && Files.notExists(outputPath.getParent())) {
            try {
                Files.createDirectories(outputPath.getParent());
            } catch (IOException e) {
                throw new WalkerException("Can't create directory for output file: " + e.getMessage());
            }
        }
    }

    private void walk() throws WalkerException {
        try (BufferedReader reader = Files.newBufferedReader(inputPath)) {
            try (BufferedWriter writer = Files.newBufferedWriter(outputPath)) {
                FileVisitor visitor = new FileVisitor(writer);
                try {
                    String path;
                    while ((path = reader.readLine()) != null) {
                        try {
                            try {
                                Files.walkFileTree(Paths.get(path), visitor);
                            } catch (InvalidPathException e) {
                                writer.write("00000000 " + path);
                            }
                        } catch (IOException e) {
                            throw new WalkerException("Failed while writing output file: " + e.getMessage());
                        }
                    }
                } catch (IOException e) {
                    throw new WalkerException("Failed while reading input file: " + e.getMessage());
                }
            } catch (IOException e) {
                throw new WalkerException("Failed to open output file: " + e.getMessage());
            }
        } catch (IOException e) {
            throw new WalkerException("Failed to open input file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
                throw new WalkerException("Expected arguments: <input file> <output file>");
            }

            new RecursiveWalk(args[0], args[1]).walk();
        } catch (WalkerException e) {
            System.err.println(e.getMessage());
        }
    }
}
