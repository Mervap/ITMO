package info.kgeorgiy.java.advanced.implementor;

import info.kgeorgiy.java.advanced.implementor.full.lang.Arabic;
import info.kgeorgiy.java.advanced.implementor.full.lang.Greek;
import info.kgeorgiy.java.advanced.implementor.full.lang.Hebrew;
import info.kgeorgiy.java.advanced.implementor.full.lang.Russian;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ClassJarImplementorTest extends ClassImplementorTest {
    @Test
    @Override
    public void test01_constructor() {
        assertConstructor(Impler.class, JarImpler.class);
    }

    @Test
    public void test15_encoding() throws IOException {
        test(false, Arabic.class);
        test(false, Hebrew.class);
        test(false, Greek.class);
        test(false, Russian.class);
    }

    @Override
    protected void implement(final Path root, final Impler implementor, final Class<?> clazz) throws ImplerException {
        super.implement(root, implementor, clazz);
        InterfaceJarImplementorTest.implementJar(root, implementor, clazz);
    }
}
