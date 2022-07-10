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
    public boolean calculatePath(MandelbrotElement el, ArrayList<double[]> path) {
        int iterator = 0;
        final Mem m = new Mem(el.originRe, el.originIm);
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
        el.setValue(iterator);
        el.setHibernatedState(iterator);
        return iterator < ITERATION_MAX;
    }
}
