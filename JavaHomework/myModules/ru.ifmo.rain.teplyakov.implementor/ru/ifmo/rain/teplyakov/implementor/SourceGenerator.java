package ru.ifmo.rain.teplyakov.implementor;

import info.kgeorgiy.java.advanced
        .implementor.ImplerException;

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implementation class which generate source code
 *
 * @author Teplyakov Valery
 */
public class SourceGenerator {

    /**
     * Space for generated classes.
     */
    private final static String SPACE = " ";

    /**
     * Line separator for generated classes.
     */
    private final static String EOLN = System.lineSeparator();

    /**
     * Semicolon for generated classes.
     */
    private final static String SEMICOLON = ";";

    /**
     * Open body bracket for generated classes.
     */
    private final static String OPEN_BODY = "{";

    /**
     * Close body bracket for generated classes.
     */
    private final static String CLOSE_BODY = "}";

    /**
     * Comma for generated classes.
     */
    private final static String COMMA = ", ";

    /**
     * The class or interface which need to implement.
     */
    private final Class<?> clazz;

    /**
     * Stores Mapping of generic types declared in each of the extensible interfaces and classes
     * to the actual type for {@link #clazz}.
     */
    private final Map<Class<?>, Map<String, String>> actualTypes;

    /**
     * Generated source code
     */
    private StringBuilder source;

    /**
     * Creates new instance of {@link SourceGenerator}.
     * Initializes the required fields, including calculating {@link #actualTypes}
     * using {@link #getActualClassTypes(Class)}.
     *
     * @param clazz class or interface which need to implement.
     */
    SourceGenerator(Class<?> clazz) throws ImplerException{
        this.clazz = clazz;
        this.source = new StringBuilder();
        this.actualTypes = getActualClassTypes(clazz);
    }

    /**
     * Generate source code for implementation of {@link #clazz}. The result is written in {@link #source}.
     * Not recalculated when re-accessed.
     *
     * @return source code in {@link String} type.
     * @throws ImplerException if generate class head (using {@link #addClassHead()}),
     *                         constructors (using {@link #addConstructors()}) or
     *                         addAbstractMethods (using {@link #addAbstractMethods()}})
     *                         failed.
     */
    public String generate() throws ImplerException {
        if (source.length() > 0) {
            return source.toString();
        }

        addPackage();
        addClassHead();
        addConstructors();
        addAbstractMethods();
        source.append(CLOSE_BODY).append(EOLN);
        return source.toString();
    }

    /**
     * Static class used for correct comparison of {@link Method}s with the same signature.
     */
    private static class MethodWrapper {

        /**
         * Create a wrapper for passed instance of {@link Method}.
         *
         * @param other instance if {@link Method}.
         */
        MethodWrapper(Method other) {
            method = other;
        }

        /**
         * Calculates hashcode for this wrapper using hashes of name, return type and parameter's types
         * of {@link #method}.
         *
         * @return hashcode for this wrapper.
         */
        @Override
        public int hashCode() {
            return ((Arrays.hashCode(method.getParameterTypes()) * 31
                    + method.getName().hashCode())) * 31
                    + method.getReturnType().hashCode();
        }

        /**
         * Compares the passed object with this wrapper for equality. Wrappers are equal, if their wrapped
         * methods have equal name, return type and parameter's types.
         *
         * @param obj the object to be compared with this wrapper.
         * @return {@code true} if specified object is equal to this wrapper and {@code false} otherwise.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            if (obj instanceof MethodWrapper) {
                return method.getName().equals(((MethodWrapper) obj).method.getName())
                        && method.getReturnType().equals(((MethodWrapper) obj).method.getReturnType())
                        && Arrays.equals(method.getParameterTypes(), ((MethodWrapper) obj).method.getParameterTypes());

            } else {
                return false;
            }
        }

        /**
         * Getter for {@link #method}.
         *
         * @return wrapped instance of {@link Method}.
         */
        public Method getMethod() {
            return method;
        }

        /**
         * Wrapped instance of {@link Method}.
         */
        private final Method method;
    }

    /**
     * Adds "Impl" suffix to simple name of given class.
     *
     * @param token class to get name.
     * @return {@link String} with receive class name.
     */
    public static String getClassName(Class<?> token) {
        return token.getSimpleName() + "Impl";
    }


    /**
     * Add package of {@link #clazz} to {@link #source}. Package is empty, if class is located in default package.
     */
    private void addPackage() {
        if (!clazz.getPackage().getName().equals("")) {
            source.append("package ").append(clazz.getPackageName()).append(SEMICOLON).append(EOLN);
        }
        source.append(EOLN);
    }

    /**
     * Analog of {@link #getTypeParametersDiamond(Type[], Map, Set, boolean)} whose {@code types} and {@code paramTypes}
     * contain the same elements and types consists only of {@link TypeVariable}.
     *
     * @param types             which need to represent.
     * @param actualClassTypes  {@link Map} of actual types for the class that owns the implemented signature.
     * @param isTypeDeclaration flag denoting the expected declaration of a type or not.
     * @return {@link String} of the resulting list of {@code types} in the expected format.
     * @throws ImplerException if failed to generate source code for some type.
     *                         This can only happen if language spec change.
     * @see #getTypeParametersDiamond(Type[], Map, Set, boolean)
     */
    private static String getTypeParametersDiamond(TypeVariable<?>[] types,
                                                   Map<String, String> actualClassTypes,
                                                   boolean isTypeDeclaration) throws ImplerException {
        return getTypeParametersDiamond(types, actualClassTypes, getSet(types), isTypeDeclaration);
    }

    /**
     * Represent the {@link TypeVariable} with upper bounds in the format accepted in the source code.
     *
     * @param type             which need to represent.
     * @param actualClassTypes {@link Map} of actual types for the class that owns the implemented signature.
     * @param paramTypes       {@link Set} set of types declared at the beginning of the method or constructor (if any),
     *                         that is, the types that do not need to be actualized.
     * @return {@link String} of the {@code type} with upper bounds in expected format
     * @throws ImplerException if failed to generate source code for some type.
     *                         This can only happen if language spec change.
     * @see #getActualType(Type, Map, Set)
     */
    private static String getTypeParametersBounds(TypeVariable<?> type,
                                                  Map<String, String> actualClassTypes,
                                                  Set<TypeVariable<?>> paramTypes) throws ImplerException {
        Type[] bounds = type.getBounds();
        if (bounds.length > 0 && !checkForObject(bounds[0])) {
            StringJoiner sj = new StringJoiner(SPACE + "&" + SPACE, SPACE + "extends" + SPACE, "");
            for (Type boundType : bounds) {
                sj.add(getActualType(boundType, actualClassTypes, paramTypes));
            }
            return sj.toString();
        }

        return "";
    }

    /**
     * Returns the resulting list of {@code types} in the format accepted in the source code.
     * Depending on the last argument it can work in 2 modes:
     * <ul>
     * <li> If {@code isTypeDeclaration = false} the expected result is considered to be part of the generic type
     * of the expandable class or method/constructor parameter. In this case, the real {@link Type} of {@code types} elements
     * can be arbitrary. Also, {@code paramTypes} must contain types declared directly before the method or constructor
     * (that is, types that do not need to be actualize), if any.</li>
     * <li> If {@code isTypeDeclaration = true} the expected result is considered to be a complete declaration of a
     * class, method, or constructor types. In this case, the elements of {@code types} must be {@link TypeVariable}.
     * It is also expected that {@code paramTypes} contain the same elements as {@code types}.</li>
     * </ul>
     *
     * @param types             which need to represent.
     * @param actualClassTypes  {@link Map} of actual types for the class that owns the implemented signature.
     * @param paramTypes        {@link Set} set of types declared at the beginning of the method or constructor (if any),
     *                          that is, the types that do not need to be actualized.
     * @param isTypeDeclaration flag denoting the expected declaration of a type or not.
     * @return {@link String} of the resulting list of {@code types} in the expected format.
     * @throws ImplerException if failed to generate source code for some type.
     *                         This can only happen if language spec change.
     * @see #getActualType(Type, Map, Set)
     */
    private static String getTypeParametersDiamond(Type[] types,
                                                   Map<String, String> actualClassTypes,
                                                   Set<TypeVariable<?>> paramTypes,
                                                   boolean isTypeDeclaration) throws ImplerException {
        if (types.length > 0) {
            StringJoiner sj = new StringJoiner(COMMA, "<", ">");
            for (Type type : types) {
                sj.add(getActualType(type, actualClassTypes, paramTypes) +
                        (isTypeDeclaration
                                ? getTypeParametersBounds((TypeVariable<?>) type, actualClassTypes, paramTypes)
                                : ""));
            }

            return sj.toString();
        }
        return "";
    }

    /**
     * Add class name and base class or implemented interface with generic type parameters
     * to {@link #source} code.
     *
     * @throws ImplerException if generation of type parameters of {@link #clazz} failed.
     * @see #getActualType(Type, Map, Set)
     */
    private void addClassHead() throws ImplerException {
        source.append("public class ").append(getClassName(clazz));
        source.append(getTypeParametersDiamond(clazz.getTypeParameters(), actualTypes.get(clazz), true));

        source.append(SPACE).append(clazz.isInterface() ? "implements" : "extends").append(SPACE)
                .append(clazz.getSimpleName());

        source.append(getTypeParametersDiamond(clazz.getTypeParameters(), actualTypes.get(clazz), false));
        source.append(SPACE).append(OPEN_BODY).append(EOLN);
    }

    /**
     * Returns tab repeated {@code cnt} times. One tab is considered equal to {@code 4} spaces.
     *
     * @param cnt number of tabs.
     * @return {@link String} with needed amount of tabs.
     */
    private static String getTabs(int cnt) {
        return SPACE.repeat(4 * cnt);
    }

    /**
     * Checks if {@code type} is {@link Object}.
     *
     * @param type which need to check.
     * @return {@code true} if {@code type} is {@link Object} and {@code false} otherwise.
     */
    private static boolean checkForObject(Type type) {
        return type instanceof Class && type.equals(Object.class);
    }

    /**
     * Returns the actual type of {@code type} with {@code $} replaced by {@code .}.
     *
     * @param type             which should be actualized.
     * @param actualClassTypes {@link Map} of actual types for the class that owns the implemented signature.
     * @param paramTypes       {@link Set} set of types declared at the beginning of the method or constructor (if any),
     *                         that is, the types that do not need to be actualized.
     * @return the actual type of {@code type}.
     * @throws ImplerException if failed to generate source code for some type.
     *                         This can only happen if language spec change.
     * @see #getActualTypeHelper(Type, Map, Set)
     */
    private static String getActualType(Type type,
                                        Map<String, String> actualClassTypes,
                                        Set<TypeVariable<?>> paramTypes) throws ImplerException {
        return getActualTypeHelper(type, actualClassTypes, paramTypes).replace('$', '.');
    }

    /**
     * Returns the actual type of {@code type}.
     * The result cannot simply be pasted into the source code because of the characters {@code $}.
     *
     * @param type             which should be actualized.
     * @param actualClassTypes {@link Map} of actual types for the class that owns the implemented signature.
     * @param paramTypes       {@link Set} set of types declared at the beginning of the method or constructor (if any),
     *                         that is, the types that do not need to be actualized.
     * @return the actual type of {@code type}.
     * @throws ImplerException if failed to generate source code for some type. Either the {@code type} has an unknown
     *                         real type or {@code type} is a {@link WildcardType} with more than one bounds
     *                         This can only happen if language spec change.
     */
    private static String getActualTypeHelper(Type type,
                                              Map<String, String> actualClassTypes,
                                              Set<TypeVariable<?>> paramTypes) throws ImplerException {

        String typeName = type.getTypeName();
        if (type instanceof Class) {
            return typeName;
        }

        if (type instanceof TypeVariable) {
            String result = paramTypes.contains(type) ? typeName : actualClassTypes.get(typeName);
            assert  result != null : typeName + " " + actualClassTypes;
            return Objects.requireNonNull(result);
        }

        if (type instanceof GenericArrayType) {
            return getActualType(((GenericArrayType) type).getGenericComponentType(), actualClassTypes, paramTypes)
                    + "[]";
        }

        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            return parameterizedType.getRawType().getTypeName() +
                    getTypeParametersDiamond(parameterizedType.getActualTypeArguments(), actualClassTypes, paramTypes, false);
        }

        if (type instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) type;

            Type[] lowerBounds = wildcardType.getLowerBounds();
            Type[] upperBounds = wildcardType.getUpperBounds();

            if (lowerBounds.length > 1 || upperBounds.length > 1
                    || (lowerBounds.length == 1 && !checkForObject(upperBounds[0]))) {
                throw new ImplerException("Can't generate type with more then one bound: " + wildcardType.getTypeName());
            }

            if (lowerBounds.length > 0) {
                return "?" + SPACE + "super" + SPACE + getActualType(lowerBounds[0], actualClassTypes, paramTypes);
            }

            if (!checkForObject(upperBounds[0])) {
                return "?" + SPACE + "extends" + SPACE + getActualType(upperBounds[0], actualClassTypes, paramTypes);
            }

            return "?";
        }

        throw new ImplerException("Can't generate class with type " + type.getClass());
    }

    /**
     * Return {@link Set} contains elements from {@code arg}.
     *
     * @param arg array which need to pack.
     * @param <T> type of array.
     * @return {@link Set} contains elements from {@code arg}.
     */
    private static <T> Set<T> getSet(T[] arg) {
        return new HashSet<>(Arrays.asList(arg));
    }

    /**
     * Takes a instance of {@link Method} or {@link Constructor} and returns it {@code return value} and {@code name}.
     * <p>
     * If the passed parameter is a {@link Method} return method's {@code return value} and {@code name}. Otherwise,
     * it's a {@link Constructor}, and because of the lack of such {@code return value} returns only the {@code name}.
     *
     * @param exec given {@link Method} or {@link Constructor}.
     * @return {@code return value} and {@code name}.
     * @throws ImplerException if generation of type of {@code return value} failed.
     */
    private String getReturnValueAndName(Executable exec) throws ImplerException {

        StringBuilder res = new StringBuilder();

        if (exec instanceof Method) {
            String returnType = getActualType(
                    ((Method) exec).getGenericReturnType(),
                    actualTypes.get(exec.getDeclaringClass()),
                    getSet(exec.getTypeParameters())
            );

            res.append(returnType).append(SPACE).append(exec.getName());
            return res.toString();
        } else {
            res.append(getClassName(exec.getDeclaringClass()));
            return res.toString();
        }
    }

    /**
     * Returns name of {@link Parameter}, optionally adding its type.
     *
     * @param param            parameter to get name from.
     * @param needTypes        flag responsible for adding parameter type.
     * @param actualClassTypes {@link Map} of actual types for the class that owns the implemented signature.
     * @param paramTypes       {@link Set} set of types declared at the beginning of the method or constructor (if any),
     *                         that is, the types that do not need to be actualized.
     * @return {@link String} representing parameter's name.
     * @throws ImplerException if generation of type of {@code param} failed.
     */
    private static String getParam(Parameter param, boolean needTypes,
                                   Map<String, String> actualClassTypes,
                                   TypeVariable<?>[] paramTypes) throws ImplerException {
        return (needTypes
                ? getActualType(param.getParameterizedType(), actualClassTypes, getSet(paramTypes)) + SPACE
                : "")
                + param.getName();
    }

    /**
     * Returns list of parameters of {@link Executable}, surrounded by round parenthesis,
     * optionally adding their types.
     *
     * @param exec        instance of {@link Executable}.
     * @param isNeedTypes flag responsible for adding parameter type.
     * @return {@link String} representing list of parameters.
     * @throws ImplerException if generation of some type of parameters failed.
     */
    private String getParams(Executable exec, boolean isNeedTypes) throws ImplerException {
        StringJoiner res = new StringJoiner(COMMA, "(", ")");
        for (Parameter param : exec.getParameters()) {
            res.add(getParam(param, isNeedTypes, actualTypes.get(exec.getDeclaringClass()), exec.getTypeParameters()));
        }
        return res.toString();
    }

    /**
     * Gets default value of given class.
     *
     * @param token class to get default value.
     * @return {@link String} representing value.
     */
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

    /**
     * If given {@link Executable} if instance of {@link Method} then return default value of return type them,
     * otherwise calls constructor of super class of given {@link Constructor}.
     *
     * @param exec given {@link Method} or {@link Constructor}.
     * @return {@link String} representing body, defined above.
     * @throws ImplerException if generation of type parameters of some methods failed.
     */
    private String getBody(Executable exec) throws ImplerException {
        if (exec instanceof Method) {
            return "return" + getDefaultValue(((Method) exec).getReturnType());
        } else {
            return "super" + getParams(exec, false);
        }
    }

    /**
     * Returns list of exceptions, that given {@link Executable} may throw. If {@code exec} is {@link Method} then
     * exception ignore because of the return default value.
     *
     * @param exec {@link Executable} to get exceptions from.
     * @return {@link String} representing list of exception.
     */
    private String getException(Executable exec) throws ImplerException {

        if (exec instanceof Constructor) {
            Type[] exceptionTypes = exec.getGenericExceptionTypes();
            if (exceptionTypes.length > 0) {
                StringJoiner joiner = new StringJoiner(COMMA, SPACE + "throws" + SPACE, "");
                for (Type exceptionType : exceptionTypes) {
                    joiner.add(exceptionType.getTypeName());
                }
                return joiner.toString();
            }
        }
        return "";
    }

    /**
     * If given {@link Executable} is a {@link Method},
     * returns a {@link String} representation of the annotation {@link Override}.
     *
     * @param exec given {@link Method} or {@link Constructor}.
     * @return {@link String} representation of the annotation {@link Override} or empty string.
     */
    private static String getOverride(Executable exec) {
        if (exec instanceof Method) {
            return getTabs(1) + "@Override" + EOLN;
        }

        return "";
    }

    /**
     * Add fully constructed {@link Executable} to {@link #source}.
     * <p>
     * That calls constructor of super class if {@code exec} is instance of {@link Constructor},
     * otherwise returns default value of return type of such {@link Method}.
     *
     * @param exec given {@link Constructor} or {@link Method}
     * @throws ImplerException if generation of type parameters of {@code exec} failed.
     */
    private void addExec(Executable exec) throws ImplerException {
        final int mods = exec.getModifiers() & ~Modifier.ABSTRACT & ~Modifier.NATIVE & ~Modifier.TRANSIENT;
        source.append(EOLN)
                .append(getOverride(exec))
                .append(getTabs(1))
                .append(Modifier.toString(mods))
                .append(mods > 0 ? SPACE : "")
                .append(getTypeParametersDiamond(exec.getTypeParameters(),
                        actualTypes.get(exec.getDeclaringClass()),
                        true))
                .append(getReturnValueAndName(exec))
                .append(getParams(exec, true))
                .append(getException(exec))
                .append(SPACE)
                .append(OPEN_BODY)
                .append(EOLN)
                .append(getTabs(2))
                .append(getBody(exec))
                .append(SEMICOLON)
                .append(EOLN)
                .append(getTabs(1))
                .append(CLOSE_BODY)
                .append(EOLN);
    }

    /**
     * Filters given array of {@link Method}, leaving only declared as abstract and puts them in given {@link Set}.
     * The method is wrapped before it is added {@link MethodWrapper}.
     *
     * @param methods        given array of {@link Method}.
     * @param methodsStorage {@link Set} where to store methods.
     */

    // NEEED FIX
    private static void getAbstractMethods(Method[] methods, Set<MethodWrapper> methodsStorage) {
        Arrays.stream(methods)
                .map(MethodWrapper::new)
                .collect(Collectors.toCollection(() -> methodsStorage));
    }

    /**
     * Add implementation of abstract methods of {@link #clazz} to {@link #source}.
     *
     * @throws ImplerException if generation of type parameters of some methods failed.
     */

    ///NEEEDFIX
    private void addAbstractMethods() throws ImplerException {
        Set<MethodWrapper> methods = new HashSet<>();

        getAbstractMethods(clazz.getMethods(), methods);
        Class<?> cur = clazz;
        while (cur != null) {
            getAbstractMethods(cur.getDeclaredMethods(), methods);
            cur = cur.getSuperclass();
        }

        methods = methods.stream()
                .filter(method -> Modifier.isAbstract(method.getMethod().getModifiers()))
                .collect(Collectors.toSet());

        for (MethodWrapper wrapper : methods) {
            addExec(wrapper.getMethod());
        }
    }

    /**
     * Add implementation of constructors of {@link #clazz} to {@link #source}.
     *
     * @throws ImplerException all constructors are private or
     *                         if generation of type parameters of some constructors failed.
     * @see #getActualType(Type, Map, Set)
     */
    private void addConstructors() throws ImplerException {
        if (clazz.isInterface()) {
            return;
        }

        Constructor<?>[] constructors = Arrays.stream(clazz.getDeclaredConstructors())
                .filter(constructor -> !Modifier.isPrivate(constructor.getModifiers()))
                .toArray(Constructor<?>[]::new);

        if (constructors.length == 0) {
            throw new ImplerException("No non-private constructors in class");
        }

        for (Constructor<?> constructor : constructors) {
            addExec(constructor);
        }
    }

    /**
     * Auxiliary method for {@link #getActualClassTypes(Class)}.
     * <p>
     * Using the already computed actual types for {@code sup}, calculates the actual type for {@code cur} and
     * writes it to {@code actualTypes}. Then, if necessary, adds to the {@code queue} classes on which the cur depends.
     *
     * @param supClass    {@link Class} which depends on {@code cur}.
     * @param cur         the class for which you want to calculate the actual types.
     * @param actualTypes expected result.
     * @param queue       the queue of classes.
     */
    private static void putTypes(Class<?> supClass, Type cur,
                                 Map<Class<?>, Map<String, String>> actualTypes,
                                 Queue<Class<?>> queue) throws ImplerException{

        if (cur instanceof ParameterizedType) {
            Class<?> curClass = (Class) ((ParameterizedType) cur).getRawType();
            Map<String, String> tmp = new HashMap<>();

            ParameterizedType parameterizedType = (ParameterizedType) cur;
            Type[] typesFrom = curClass.getTypeParameters();
            Type[] typesTo = parameterizedType.getActualTypeArguments();
            for (int i = 0; i < typesFrom.length; ++i) {
                System.out.println(actualTypes.get(supClass) + " " + typesTo[i].getTypeName());
                tmp.put(typesFrom[i].getTypeName(), Objects.requireNonNull(getActualType(typesTo[i], actualTypes.get(supClass), Collections.emptySet())));
            }

            System.out.println(supClass + " " + cur + " " + tmp);
            actualTypes.put(curClass, tmp);
            queue.add(curClass);
        }
    }

    /**
     * Builds mapping of generic types of classes and interfaces to the actual type for all classes and interfaces
     * on which depends {@code token}.
     *
     * @param token {@link Class} with actual types.
     * @return {@link Map} with actual types.
     * @see #putTypes(Class, Type, Map, Queue)
     */
    private static Map<Class<?>, Map<String, String>> getActualClassTypes(Class<?> token) throws ImplerException {
        Map<Class<?>, Map<String, String>> actualTypes = new HashMap<>();
        Map<String, String> tmp = new HashMap<>();
        for (Type type : token.getTypeParameters()) {
            tmp.put(type.getTypeName(), type.getTypeName());
        }
        actualTypes.put(token, tmp);

        Queue<Class<?>> queue = new ArrayDeque<>();
        queue.add(token);
        while (!queue.isEmpty()) {
            Class<?> cur = queue.poll();
            Type sup = cur.getGenericSuperclass();

            if (sup != null) {
                putTypes(cur, sup, actualTypes, queue);
            }

            for (Type in : cur.getGenericInterfaces()) {
                putTypes(cur, in, actualTypes, queue);
            }
        }

        return actualTypes;
    }
}
