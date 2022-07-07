package fine.fractals.fractal.finebrot.phoenix;

import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.MemPhoenix;
import fine.fractals.fractal.finebrot.common.FinebrotFractalImpl;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;

public abstract class FractalPhoenix extends FinebrotFractalImpl {

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
    public boolean calculatePath(MandelbrotElement el, ArrayList<double[]> path) {
        int iterator = 0;

        final MemPhoenix m = new MemPhoenix();
        m.re = el.originRe;
        m.im = el.originIm;
        while (m.quadrance() < CALCULATION_BOUNDARY && iterator < ITERATION_MAX) {

            math(m, el.originRe, el.originIm);

            if (AreaFinebrot.contains(m)) {
                path.add(new double[]{m.re, m.im});
            }
            iterator++;
        }
        el.setValues(iterator);
        return iterator < ITERATION_MAX;
    }
}
