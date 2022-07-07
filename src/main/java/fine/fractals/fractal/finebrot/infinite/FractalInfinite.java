package fine.fractals.fractal.finebrot.infinite;

import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.Mem;
import fine.fractals.fractal.finebrot.finite.FractalFinite;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;

public abstract class FractalInfinite extends FractalFinite {

    public FractalInfinite() {
    }

    @Override
    public boolean calculatePath(MandelbrotElement el, ArrayList<double[]> path) {
        int iterator = 0;

        final Mem m = new Mem();
        m.re = el.originRe;
        m.im = el.originIm;
        while (m.quadrance() < CALCULATION_BOUNDARY && iterator < ITERATION_MAX) {

            math(m, el.originRe, el.originIm);

            if (AreaFinebrot.contains(m)) {
                /* Calculation did not diverge */
                path.add(new double[]{m.re, m.im});
            }
            iterator++;
        }
        el.setValues(iterator);

        /*
         * Finite fractal
         */
        return iterator == ITERATION_MAX;
    }
}
