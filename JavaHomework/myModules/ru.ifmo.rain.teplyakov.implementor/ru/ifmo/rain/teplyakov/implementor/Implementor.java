package ru.ifmo.rain.teplyakov.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;

/**
 * Implementation class for {@link JarImpler} interface
 *
 * @author Teplyakov Valery
 */
public class Implementor implements JarImpler {

    /**
     * Filename extension for source java files
     */
    private static final String JAVA = ".java";

    /**
     * Filename extension for compiled class files
     */
    private static final String CLASS = ".class";

    /**
     * Instance of {@link SimpleFileVisitor} which deletes visited files and directories.
     */
    private static final SimpleFileVisitor<Path> DELETE_VISITOR = new SimpleFileVisitor<>() {
        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            Files.delete(file);
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
            Files.delete(dir);
            return FileVisitResult.CONTINUE;
        }
    };

    /**
     * Creates new instance of {@link Implementor}
     */
    public Implementor() {
    }

    /**
     * Recursively clean up the directory with path {@code root} by {@link #DELETE_VISITOR}.
     *
     * @param root path of directory for clean.
     * @throws IOException if an I/O error is thrown by a {@link #DELETE_VISITOR} method.
     */
    private static void clean(final Path root) throws IOException {
        if (Files.exists(root)) {
            Files.walkFileTree(root, DELETE_VISITOR);
        }
    }

    /**
     * Return path to file, containing implementation of given class
     * located along the path relative to the {@code root}.
     *
     * @param token {@code class} to get name from.
     * @param root  path to parent directory of {@code class}.
     * @param tail  file extension.
     * @return {@link Path} representing path to expected file.
     */
    private static Path getFilePath(Class<?> token, Path root, String tail) {
        return root.resolve(token.getPackageName().replace('.', File.separatorChar))
                .resolve(SourceGenerator.getClassName(token) + tail);
    }

    /**
     * Creates parent directory for file {@code file}.
     *
     * @param path file to create parent directory.
     * @throws ImplerException if an error occurred during the creation.
     */
    private static void createDirectories(Path path) throws ImplerException {
        if (path.getParent() != null) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new ImplerException("Can't create directories for output file", e);
            }
        }
    }

    /**
     * Checks if any of given arguments is {@code null}.
     *
     * @param args list of arguments.
     * @throws ImplerException if any arguments is {@code null}.
     */
    private static void checkForNull(Object... args) throws ImplerException {
        for (Object arg : args) {
            if (arg == null) {
                throw new ImplerException("Not-null arguments expected");
            }
        }
    }

    /**
     * Trying to get ClassPath on a given {@link Class}.
     *
     * @param token {@code class} for getting ClassPath.
     * @return ClassPath in {@code URI} form or null if doesn't exit.
     * @throws ImplerException if fails to get ClassPath.
     */
    private static String getClassPath(Class<?> token) throws ImplerException {
        try {
            CodeSource codeSource = token.getProtectionDomain().getCodeSource();
            if (codeSource == null) {
                return null;
            }

            return Path.of(codeSource.getLocation().toURI()).toString();
        } catch (URISyntaxException e) {
            throw new ImplerException("URL cannot be converted to a URI: ", e);
        }
    }

    /**
     * Produces {@code .class} file from given {@code .java} class or interface
     * specified by provided {@link Class} {@code token}.
     *
     * @param token   type token to compile.
     * @param tempDir temporary folder with generated {@code .java} file.
     * @throws ImplerException if the given class cannot be compiled for one of such reasons:
     *                         <ul>
     *                         <li> Unable to find java compiler.</li>
     *                         <li> Compiler exit code not {@code 0}.</li>
     *                         <li> {@link #getClassPath(Class)} failed to getting ClassPath. </li>
     *                         </ul>
     */
    private static void compile(Class<?> token, Path tempDir) throws ImplerException {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        if (compiler == null) {
            throw new ImplerException("Could not find java compiler");
        }

        List<String> args = new ArrayList<>();
        args.add(getFilePath(token, tempDir, JAVA).toString());
        String classPath = getClassPath(token);
        if (classPath != null) {
            args.add("-cp");
            args.add(classPath);
        }

        int exitCode = compiler.run(null, null, null, args.toArray(String[]::new));
        if (exitCode != 0) {
            throw new ImplerException("Compiler exit code: " + exitCode);
        }
    }

    /**
     * Produces {@code .jar} file implementing class or interface specified by provided {@code token}.
     * <p>
     * Generated class full name same as classes name of the type token with {@code Impl} suffix added.
     * <p>
     * During implementation creates temporary folder to store temporary {@code .java} and {@code .class} files.
     * If program fails to delete temporary folder, it informs user about it.
     *
     * @param token   type token to create implementation for.
     * @param jarFile target {@code .jar} file.
     * @throws ImplerException if the given class cannot be generated for one of such reasons:
     *                         <ul>
     *                         <li> Some arguments are {@code null}</li>
     *                         <li> Error occurs during implementation via {@link #implement(Class, Path)} </li>
     *                         <li> {@link #compile(Class, Path)} failed to compile implemented class </li>
     *                         <li> The problems with I/O occurred during implementation. </li>
     *                         </ul>
     */
    @Override
    public void implementJar(Class<?> token, Path jarFile) throws ImplerException {
        checkForNull(token, jarFile);
        createDirectories(jarFile);

        Path tempDir;
        try {
            tempDir = Files.createTempDirectory(jarFile.toAbsolutePath().getParent(), "temp");
        } catch (IOException e) {
            throw new ImplerException("Unable to create temp directory", e);
        }

        try {
            implement(token, tempDir);
            compile(token, tempDir);

            Manifest manifest = new Manifest();
            Attributes attributes = manifest.getMainAttributes();
            attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            try (JarOutputStream writer = new JarOutputStream(Files.newOutputStream(jarFile), manifest)) {
                writer.putNextEntry(new ZipEntry(token.getName().replace('.', '/') + "Impl" + CLASS));
                Files.copy(getFilePath(token, tempDir, CLASS), writer);
            } catch (IOException e) {
                throw new ImplerException("Unable to write to JAR file", e);
            }
        } finally {
            try {
                clean(tempDir);
            } catch (IOException e) {
                System.err.println("Unable to delete temp directory: " + e.getMessage());
            }
        }
    }

    /**
     * Converts given string to unicode escaping
     *
     * @param in {@link String} to convert
     * @return converted string
     */
    private static String toUnicode(String in) {
        StringBuilder b = new StringBuilder();
        for (char c : in.toCharArray()) {
            if (c >= 128) {
                b.append(String.format("\\u%04X", (int) c));
            } else {
                b.append(c);
            }
        }
        return b.toString();
    }

    /**
     * Produces code implementing class or interface specified by provided {@code token}.
     * <p>
     * Generated class full name same as classes name of the type token with {@code Impl} suffix added.
     * Generated source code placed in the subdirectory of the specified {@code root} directory.
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws ImplerException if the given class cannot be generated for one of such reasons:
     *                         <ul>
     *                         <li> Some arguments are {@code null}</li>
     *                         <li> Given {@code class} is primitive or array. </li>
     *                         <li> Given {@code class} is final class or {@link Enum}. </li>
     *                         <li> {@link SourceGenerator#generate()} failed to generate source </li>
     *                         <li> The problems with I/O occurred during implementation. </li>
     *                         </ul>
     */
    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        checkForNull(token, root);

        if (token.isPrimitive() || token == Enum.class || token.isArray() || Modifier.isFinal(token.getModifiers())) {
            throw new ImplerException("Incorrect class token");
        }

        root = getFilePath(token, root, JAVA);
        createDirectories(root);
        try (BufferedWriter writer = Files.newBufferedWriter(root)) {
            SourceGenerator sourceGenerator = new SourceGenerator(token);
            writer.write(toUnicode(sourceGenerator.generate()));
        } catch (IOException e) {
            throw new ImplerException("Can't write into output file", e);
        }

    }

    /**
     * This function is used to choose which way of implementation.
     * <ul>
     * <li> If passed 2 arguments: {@code className rootPath} - runs {@link #implement(Class, Path)}</li>
     * <li> If passed 3 arguments: {@code -jar className jarPath} - runs {@link #implementJar(Class, Path)} with two second arguments</li>
     * </ul>
     * If arguments are incorrect or an error occurred during generation print message with information about error
     *
     * @param args arguments for running an application
     */
    public static void main(String[] args) {
        if (args == null || (args.length != 2 && args.length != 3)) {
            System.err.println("<class name> <root of output file> or");
            System.err.println("-jar <class name> <output .jar file> expected");
            return;
        }

        for (String arg : args) {
            if (arg == null) {
                System.err.println("All arguments must be non-null");
                return;
            }
        }

        if (args.length == 3 && !args[2].endsWith(".jar")) {
            System.err.println("Specify the correct jar output file");
            return;
        }

        JarImpler implementor = new Implementor();

        try {
            if (args.length == 2) {
                implementor.implement(Class.forName(args[0]), Paths.get(args[1]));
            } else {
                implementor.implementJar(Class.forName(args[1]), Paths.get(args[2]));
            }
        } catch (InvalidPathException e) {
            System.err.println("Incorrect path to root: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Incorrect class name: " + e.getMessage());
        } catch (ImplerException e) {
            System.err.println("An error occurred during implementation: " + e.getMessage());
        }
    }
}
