package fine.fractals.fractal.finebrot.common;

import fine.fractals.data.annotation.ThreadSafe;
import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.fractal.mandelbrot.MandelbrotImpl;

import java.util.ArrayList;

public abstract class FinebrotCpu extends FinebrotAbstractImpl {

    public FinebrotCpu() {
        Mandelbrot = new MandelbrotImpl(this);
    }

    @ThreadSafe
    public abstract boolean calculatePath(MandelbrotElement el, final ArrayList<double[]> path);

}
