package fine.fractals.fractal.finebrot.euler;

import fine.fractals.data.annotation.ThreadSafe;
import fine.fractals.data.mandelbrot.MandelbrotElement;
import fine.fractals.data.mem.MemEuler;
import fine.fractals.fractal.finebrot.common.FinebrotFractalImpl;
import fine.fractals.perfect.coloring.EulerPerfectColorDistributionImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

import static fine.fractals.fractal.finebrot.AreaFinebrotImpl.AreaFinebrot;
import static fine.fractals.fractal.finebrot.euler.PixelsEulerFinebrotImpl.Spectra.*;
import static fine.fractals.math.MathematicianImpl.Mathematician;

public abstract class FractalEuler extends FinebrotFractalImpl {

    private static final Logger log = LogManager.getLogger(FractalEuler.class);

    public static PixelsEulerFinebrotImpl PixelsEulerFinebrot = new PixelsEulerFinebrotImpl();

    public FractalEuler() {
        log.info("FractalEuler()");
        PerfectColorDistribution = new EulerPerfectColorDistributionImpl();
        PathsFinebrot = new PathsEulerFinebrotImpl();
    }

    public abstract void math(MemEuler m, double re, double im);

    @Override
    @ThreadSafe
    public boolean calculatePath(MandelbrotElement el, ArrayList<double[]> path) {
        int iterator = 0;
        final MemEuler m = new MemEuler(el.originRe, el.originIm);
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
        el.setValues(iterator);
        return iterator < ITERATION_MAX;
    }

    public static void colorsFor(MemEuler m, int elementIndex, int pathLength) {
        if (Mathematician.isPrime(elementIndex)) {
            m.spectra = red;
            return;
        }
        if (Mathematician.isPrime(pathLength)) {
            m.spectra = green;
            return;
        }
        m.spectra = blue;
    }
}
