package fine.fractals.data.mandelbrot;

import static fine.fractals.data.mandelbrot.MandelbrotPixelState.ActiveNew;
import static fine.fractals.data.mandelbrot.MandelbrotPixelState.HibernatedDeepBlack;

public abstract class MandelbrotElementFactory {

    public static MandelbrotElement activeNew(double re, double im) {
        return new MandelbrotElement(re, im, ActiveNew);
    }

    public static MandelbrotElement hibernatedDeepBlack(double re, double im) {
        return new MandelbrotElement(re, im, HibernatedDeepBlack);
    }
}
