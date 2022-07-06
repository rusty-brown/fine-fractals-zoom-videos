package fine.fractals.fractal.abst;

import fine.fractals.data.MandelbrotElement;
import fine.fractals.data.MemPhoenix;

import java.util.ArrayList;

import static fine.fractals.context.finebrot.AreaFinebrotImpl.AreaFinebrot;

public abstract class FractalPhoenix extends Fractal {

    /*
     * Phoenix fractal parameters
     * c, p
     */
    protected double c;
    protected double p;


    public FractalPhoenix(String name) {
        super(name);
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
        return iterator < Fractal.ITERATION_MAX;
    }
}
