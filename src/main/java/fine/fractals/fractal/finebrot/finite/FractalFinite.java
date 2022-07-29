package fine.fractals.fractal.finebrot.finite;

import fine.fractals.data.annotation.ThreadSafe;
import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.data.mem.Mem;
import fine.fractals.fractal.finebrot.common.FinebrotCommonImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;

public abstract class FractalFinite extends FinebrotCommonImpl {

    private static final Logger log = LogManager.getLogger(FractalFinite.class);

    public FractalFinite() {
        log.debug("constructor");
    }

    public abstract void math(Mem m, double re, double im);

    @Override
    @ThreadSafe
    public ArrayList<double[]> calculatePath(MandelbrotElement el) {
        int iterator = 0;
        int length = 0;
        final Mem m = new Mem(el.originRe, el.originIm);
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
