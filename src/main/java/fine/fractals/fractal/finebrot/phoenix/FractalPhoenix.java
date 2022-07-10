package fine.fractals.fractal.finebrot.phoenix;

import fine.fractals.data.annotation.ThreadSafe;
import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.data.mem.MemPhoenix;
import fine.fractals.fractal.finebrot.common.FinebrotCommonImpl;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;

public abstract class FractalPhoenix extends FinebrotCommonImpl {

    /*
     * Phoenix fractal parameters
     * c, p
     */
    protected double c;
    protected double p;

    public FractalPhoenix() {
    }

    public abstract void math(MemPhoenix m, double re, double im);

    @Override
    @ThreadSafe
    public boolean calculatePath(MandelbrotElement el, ArrayList<double[]> path) {
        int iterator = 0;
        final MemPhoenix m = new MemPhoenix(el.originRe, el.originIm);
        while (m.quadrance() < CALCULATION_BOUNDARY && iterator < ITERATION_MAX) {
            /*
             * fractal calculation
             */
            math(m, el.originRe, el.originIm);

            if (AreaFinebrot.contains(m)) {
                path.add(new double[]{m.re, m.im});
            }
            iterator++;
        }
        el.setHibernatedState(iterator);
        return iterator < ITERATION_MAX;
    }
}
