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
    public static double phoenix_initializer;

    public FractalPhoenix() {
    }

    public abstract void math(MemPhoenix m, double re, double im);

    @Override
    @ThreadSafe
    public ArrayList<double[]> calculatePath(MandelbrotElement el) {
        int iterator = 0;
        int length = 0;
        final MemPhoenix m = new MemPhoenix(el.originRe, el.originIm);
        while (m.quadrance() < CALCULATION_BOUNDARY && iterator < ITERATION_MAX) {

            /*
             * Investigate if this is a good calculation path
             * Don't create path data yet. Too many origin's don't produce good data
             * Most long expensive calculations end up inside Mandelbrot set
             */
            math(m, el.originRe, el.originIm);
            if (AreaFinebrot.contains(m)) {
                length++;
            }
            iterator++;
        }
        el.setFinishedState(iterator, length);

        /* Verify divergent path length */
        if (length > ITERATION_min && iterator < ITERATION_MAX) {

            /*
             * This origin produced good data, record calculation path
             */
            m.re = el.originRe;
            m.im = el.originIm;
            m.prev_re = phoenix_initializer;
            m.prev_im = phoenix_initializer;
            m.prev_prev_re = phoenix_initializer;
            m.prev_prev_im = phoenix_initializer;

            final ArrayList<double[]> path = new ArrayList<>(length);
            for (int i = 0; i < iterator; i++) {
                math(m, el.originRe, el.originIm);
                if (AreaFinebrot.contains(m)) {
                    path.add(new double[]{m.re, m.im});
                }
            }
            return path;
        } else {
            return null;
        }
    }
}
