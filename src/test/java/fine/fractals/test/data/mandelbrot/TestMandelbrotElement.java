package fine.fractals.test.data.mandelbrot;

import fine.fractals.data.mandelbrot.MandelbrotElement;
import org.junit.Test;

import static fine.fractals.data.mandelbrot.MandelbrotElementFactory.activeNew;
import static fine.fractals.data.mandelbrot.MandelbrotElementFactory.hibernatedDeepBlack;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestMandelbrotElement {

    @Test
    public void test() {
        final MandelbrotElement better = activeNew(0, 0);
        final MandelbrotElement worse = hibernatedDeepBlack(0, 0);

        assertTrue(better.compareTo(worse) < 0);
        assertTrue(worse.compareTo(better) > 0);

        assertTrue(worse.hasWorseStateThen(better));
        assertFalse(better.hasWorseStateThen(worse));
    }
}
