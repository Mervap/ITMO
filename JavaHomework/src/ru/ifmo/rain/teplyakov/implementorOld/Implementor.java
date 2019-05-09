package ru.ifmo.rain.teplyakov.implementor;

import info.kgeorgiy.java.advanced.implementor.Impler;
import info.kgeorgiy.java.advanced.implementor.ImplerException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.net.IDN.toUnicode;

public class Implementor implements Impler {

    private final static String SPACE = " ";
    private final static String TAB = "    ";
    private final static String EOLN = System.lineSeparator();
    private final static String SEMICOLON = ";";
    private final static String LEFT_BRACE = "{";
    private final static String RIGHT_BRACE = "}";
    private final static String COMMA = ",";
    private final static String CLASS_SUFFIX = "Impl";

    private Path getFilePath(Class<?> token, Path root) {
        return root.resolve(token.getPackageName().replace('.', File.separatorChar)).resolve(token.getSimpleName() + CLASS_SUFFIX + ".java");
    }

    private static String getPackage(Class<?> token) {
        StringBuilder res = new StringBuilder();
        if (!token.getPackage().getName().equals("")) {
            res.append("package ").append(token.getPackageName()).append(SEMICOLON).append(EOLN);
        }
        res.append(EOLN);
        return res.toString();
    }

    private static String getClassName(Class<?> token) {
        return token.getSimpleName() + CLASS_SUFFIX;
    }

    private static String getClassHead(Class<?> token) {
        return getPackage(token) + "public class " + getClassName(token) +
                (token.isInterface() ? " implements " : " extends ") + token.getSimpleName() + SPACE + LEFT_BRACE + EOLN;
    }

    private static void createDirectories(Path path) throws ImplerException {
        if (path.getParent() != null) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException e) {
                throw new ImplerException("Can't create directories for output file", e);
            }
        }
    }

    private static String getTabs(long cnt) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < cnt; ++i) {
            res.append(TAB);
        }

        return res.toString();
    }

    private static String getReturnValueWildCardAndName(Executable exec) {

        StringBuilder res = new StringBuilder();

        TypeVariable<?>[] paramTypes = exec.getTypeParameters();
        if (paramTypes.length > 0) {
            StringJoiner sj = new StringJoiner(",", "<", "> ");
            for (TypeVariable<?> paramType : paramTypes) {
                sj.add(paramType.getTypeName());
            }
            res.append(sj.toString());
        }

        if (exec instanceof Method) {
            Method method = (Method) exec;
            var a = method.getDeclaringClass().getTypeParameters();
            res.append(method.getGenericReturnType().getTypeName().replaceAll("\\$", ".")).append(SPACE).append(exec.getName());
            return res.toString();
        } else {
            res.append(getClassName(((Constructor<?>) exec).getDeclaringClass()));
            return res.toString();
        }
    }

    private static String getParam(Type type, Parameter param, boolean needTypes) {
        return (needTypes ? type.getTypeName().replaceAll("\\$", ".") + SPACE : "") + param.getName();
    }

    private static String getParams(Executable exec, boolean needTypes) {
        StringJoiner res = new StringJoiner(COMMA + SPACE, "(", ")");
        Parameter[] params = exec.getParameters();
        Type[] types = exec.getGenericParameterTypes();
        for (int i = 0; i < params.length; ++i) {
            res.add(getParam(types[i], params[i], needTypes));
        }
        return res.toString();
    }

    private static String getDefaultValue(Class<?> token) {
        if (token.equals(boolean.class)) {
            return " false";
        } else if (token.equals(void.class)) {
            return "";
        } else if (token.isPrimitive()) {
            return " 0";
        }
        return " null";
    }

    private static String getBody(Executable exec) {
        if (exec instanceof Method) {
            return "return" + getDefaultValue(((Method) exec).getReturnType());
        } else {
            return "super" + getParams(exec, false);
        }
    }

    private static String getException(Executable exec) {
        if (exec instanceof Constructor) {
            Type[] exceptionTypes = exec.getGenericExceptionTypes();
            if (exceptionTypes.length > 0) {
                StringJoiner joiner = new StringJoiner(",", " throws ", " ");
                for (Type exceptionType : exceptionTypes) {
                    joiner.add(exceptionType.getTypeName());
                }
                return joiner.toString();
            }
        }
        return "";
    }

    private static String getOverride(Executable exec) {
        if (exec instanceof Method) {
            return getTabs(1) + "@Override" + EOLN;
        }

        return "";
    }

    private static String implementExec(Executable exec) {
        StringBuilder res = new StringBuilder(EOLN);
        final int mods = exec.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.NATIVE & ~Modifier.TRANSIENT;

        res.append(getOverride(exec))
                .append(getTabs(1))
                .append(Modifier.toString(mods))
                .append(mods > 0 ? SPACE : "")
                .append(getReturnValueWildCardAndName(exec))
                .append(getParams(exec, true))
                .append(getException(exec))
                .append(LEFT_BRACE)
                .append(EOLN)
                .append(getTabs(2))
                .append(getBody(exec))
                .append(SEMICOLON)
                .append(EOLN)
                .append(getTabs(1))
                .append(RIGHT_BRACE)
                .append(EOLN);

        return res.toString();
    }

    private static class MethodWrapper {

        MethodWrapper(Method other) {
            method = other;
        }

        @Override
        public int hashCode() {
            return ((Arrays.hashCode(method.getGenericParameterTypes()) * 31
                    + method.getName().hashCode())) * 31
                    + method.getGenericReturnType().hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof MethodWrapper) {
                return method.getName().equals(((MethodWrapper) obj).method.getName())
                        && method.getGenericReturnType().equals(((MethodWrapper) obj).method.getGenericReturnType())
                        && Arrays.equals(method.getGenericParameterTypes(), ((MethodWrapper) obj).method.getGenericParameterTypes());

            } else {
                return false;
            }
        }

        public Method getMethod() {
            return method;
        }

        Method method;
    }

    private static void getAbstractMethods(Method[] methods, Set<MethodWrapper> methodsStorage) {
        Arrays.stream(methods)
                .filter(method -> Modifier.isAbstract(method.getModifiers()))
                .map(MethodWrapper::new)
                .collect(Collectors.toCollection(() -> methodsStorage));
    }

    private static void implementAbstractMethods(Class<?> token, BufferedWriter writer) throws IOException {
        HashSet<MethodWrapper> methods = new HashSet<>();
        var a = token.getTypeName();
        var b = token.getGenericSuperclass();
        var bb = token.getGenericInterfaces();
        var c = token.getTypeParameters();
        var d = token.getGenericInterfaces();

        getAbstractMethods(token.getMethods(), methods);
        while (token != null) {
            getAbstractMethods(token.getDeclaredMethods(), methods);
            token = token.getSuperclass();
        }

        for (MethodWrapper wrapper : methods) {
            writer.write(toUnicode(implementExec(wrapper.getMethod())));
        }
    }


    private static void implementConstructors(Class<?> token, BufferedWriter writer) throws IOException, ImplerException {
        Constructor<?>[] constructors = Arrays.stream(token.getDeclaredConstructors())
                .filter(constructor -> !Modifier.isPrivate(constructor.getModifiers()))
                .toArray(Constructor<?>[]::new);

        if (constructors.length == 0) {
            throw new ImplerException("No non-private constructors in class");
        }

        for (Constructor<?> constructor : constructors) {
            writer.write(toUnicode(implementExec(constructor)));
        }
    }

    @Override
    public void implement(Class<?> token, Path root) throws ImplerException {
        if (token == null || root == null) {
            throw new ImplerException("Not-null arguments expected");
        }

        if (token.isPrimitive() || token == Enum.class || token.isArray() || Modifier.isFinal(token.getModifiers())) {
            throw new ImplerException("Incorrect class token");
        }

        root = getFilePath(token, root);
        createDirectories(root);
        try (BufferedWriter writer = Files.newBufferedWriter(root)) {
            writer.write(toUnicode(getClassHead(token)));
            if (!token.isInterface()) {
                implementConstructors(token, writer);
            }
            implementAbstractMethods(token, writer);
            writer.write(toUnicode(RIGHT_BRACE + EOLN));
        } catch (IOException e) {
            throw new ImplerException("Can't write into output file", e);
        }

    }

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            System.err.println("<class name> <root of output file> expected");
            return;
        }

        for (String arg : args) {
            if (arg == null) {
                System.err.println("All arguments must be non-null");
                return;
            }
        }

        Impler implementor = new Implementor();

        try {
            implementor.implement(Class.forName(args[0]), Paths.get(args[1]));
        } catch (InvalidPathException e) {
            System.err.println("Incorrect path to root: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Incorrect class name: " + e.getMessage());
        } catch (ImplerException e) {
            System.err.println("An error occurred during implementation: " + e.getMessage());
        }
    }
}
