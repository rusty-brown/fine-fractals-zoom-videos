package fine.fractals.fractal.abst;

import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.Mem;

import java.util.ArrayList;

import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;

public abstract class FractalInfinite extends Fractal {

    public FractalInfinite(String name) {
        super(name);
    }

    public abstract void math(Mem m, double re, double im);

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
        return iterator == Fractal.ITERATION_MAX;
    }
}